package me.ryan_s.data;

/**
 * Created by SimplyBallistic on 19/07/2019
 *
 * @author SimplyBallistic
 **/
public enum FinancialStatus {//outlines the financial statuses
    FULLY_PAID("Fully Paid", "success"), PARTLY_PAID("Partially Paid", "info"), UNFINANCIAL("Unfinancial", "danger");

    private String name, style;//we also assign a bootstrap style to help theme the boxes

    FinancialStatus(String name, String style) {

        this.name = name;
        this.style = style;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getStyle() {
        return style;
    }
}
