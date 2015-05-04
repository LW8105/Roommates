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

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public double getDividedAmount() {
        return dividedAmount;
    }

    public void setDividedAmount(double dividedAmount) {
        this.dividedAmount = dividedAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getNotes() {
        return notes;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
