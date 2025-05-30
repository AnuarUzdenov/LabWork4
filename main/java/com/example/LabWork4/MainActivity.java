import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLHelper dbHelper;
    private LinearLayout repairsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLHelper(this);
        repairsContainer = findViewById(R.id.repairsContainer);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        btnAdd.setOnClickListener(v -> addTestData());
        btnRefresh.setOnClickListener(v -> displayAllRepairs());

        displayAllRepairs();
    }

    private void addTestData() {
        dbHelper.addRepair("Иванов И.И.", "iPhone 12", "Разбит экран", 5000, false);
        dbHelper.addRepair("Петров П.П.", "Samsung S21", "Не работает микрофон", 3000, true);
        dbHelper.addRepair("Сидоров С.С.", "Xiaomi Mi 11", "Залит водой", 7000, false);
        displayAllRepairs();
    }

    private ArrayList<Repair> getAllRepairs() {
        ArrayList<Repair> repairs = new ArrayList<>();
        Cursor cursor = dbHelper.getAllRepairs();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLHelper.COL_ID));
                String client = cursor.getString(cursor.getColumnIndexOrThrow(SQLHelper.COL_CLIENT));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(SQLHelper.COL_PHONE));
                String issue = cursor.getString(cursor.getColumnIndexOrThrow(SQLHelper.COL_ISSUE));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(SQLHelper.COL_PRICE));
                boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(SQLHelper.COL_STATUS)) == 1;

                repairs.add(new Repair(id, client, phone, issue, price, status));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return repairs;
    }

    private void displayAllRepairs() {
        repairsContainer.removeAllViews();
        ArrayList<Repair> repairs = getAllRepairs();

        for (Repair repair : repairs) {
            LinearLayout repairLayout = new LinearLayout(this);
            repairLayout.setOrientation(LinearLayout.VERTICAL);
            repairLayout.setPadding(0, 16, 0, 16);

            TextView tvInfo = new TextView(this);
            tvInfo.setText(String.format("Клиент: %s\nТелефон: %s\nНеисправность: %s\nЦена: %d руб.\nСтатус: %s",
                    repair.client, repair.phone, repair.issue, repair.price,
                    repair.status ? "Готов" : "В работе"));

            Button btnStatus = new Button(this);
            btnStatus.setText(repair.status ? "Отметить как 'В работе'" : "Отметить как 'Готов'");
            btnStatus.setOnClickListener(v -> {
                dbHelper.updateRepairStatus(repair.id, !repair.status);
                displayAllRepairs();
            });

            Button btnDelete = new Button(this);
            btnDelete.setText("Удалить");
            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteRepair(repair.id);
                displayAllRepairs();
            });

            repairLayout.addView(tvInfo);
            repairLayout.addView(btnStatus);
            repairLayout.addView(btnDelete);
            repairsContainer.addView(repairLayout);
        }
    }
}