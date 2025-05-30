public class Repair {
    public int id;
    public String client;
    public String phone;
    public String issue;
    public int price;
    public boolean status;

    public Repair(int id, String client, String phone, String issue, int price, boolean status) {
        this.id = id;
        this.client = client;
        this.phone = phone;
        this.issue = issue;
        this.price = price;
        this.status = status;
    }
}