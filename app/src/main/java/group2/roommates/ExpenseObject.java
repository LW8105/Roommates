package group2.roommates;

public class ExpenseObject {

    private int id;
    private String expenseAmount;
    private double dividedAmount;
    private String dueDate;
    private String expenseName;
    private String author;
    private String notes;
    private boolean paid;

    public ExpenseObject(int id, String expenseAmount, double dividedAmount, String dueDate, String expenseName,
                         String author, String description, boolean paid) {
        this.id = id;
        this.expenseAmount = expenseAmount;
        this.dividedAmount = dividedAmount;
        this.dueDate = dueDate;
        this.author = author;
        this.expenseName = expenseName;
        this.notes = description;
        this.paid = paid;
    }

    public int getid() {
        return id;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public double getDividedAmount() {
        return dividedAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isPaid() {
        return paid;
    }
}
