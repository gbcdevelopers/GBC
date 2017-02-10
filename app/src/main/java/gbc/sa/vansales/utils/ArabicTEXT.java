package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 10-Feb-17.
 */
public class ArabicTEXT {
    public static String AMOUNT;
    public static String AmountPaid;
    public static String BadReturn;
    public static String CASEPRICE;
    public static String Cash;
    public static String Cashinvoice;
    public static String Creditinvoice;
    public static String Customer;
    public static String DATE;
    public static String DESCRIPTION;
    public static String DISCOUNT;
    public static String DraftCopy;
    public static String DuplicateCopy;
    public static String Free;
    public static String GoodReturn;
    public static String Invoice;
    public static String InvoiceAmount;
    public static String InvoiceBalance;
    public static String InvoiceDate;
    public static String InvoiceDiscount;
    public static String Item;
    public static String LineDiscount;
    public static String NetSales;
    public static String OUTLET;
    public static String OriginalCopy;
    public static String OtherDiscount;
    public static String PaymentDetails;
    public static String PromotionFree;
    public static String QTY;
    public static String ROUTE;
    public static String Receipt;
    public static String SALESMAN;
    public static String Sales;
    public static String Salesman;
    public static String SubTotal;
    public static String TCcharged;
    public static String TIME;
    public static String TOTAL;
    public static String Total;
    public static String UNITPRICE;
    public static String UPC;
    public static String headerArabic;
    public static String headerDotmatbottomrevereArab;
    public static String headerDotmatrevereseArabic;
    public static String headerbottomrevereseArabic;
    public static String headerminiDotmatbottomrevereArabic;
    public static String headerminiDotmatrevereseArabic;
    public static String headerminibottomrevereseArabic;
    public static String headerminirevereseArabic;
    public static String headerrevereseArabic;

    static {
        Sales = "\u0645\u0628\u064a\u0639\u0627\u062a";
        Free = "\u0645\u062c\u0627\u0646\u0627";
        PromotionFree = "\u062a\u0639\u0632\u064a\u0632 \u0627\u0644\u062d\u0631\u0629";
        GoodReturn = "\u0627\u0644\u0645\u0631\u062a\u062c\u0639\u0627\u062a \u0627\u0644\u062c\u064a\u062f\u0629";
        BadReturn = "\u0627\u0644\u0645\u0631\u062a\u062c\u0639\u0627\u062a \u0627\u0644\u062a\u0627\u0644\u0641\u0629";
        Customer = "\u0639\u0645\u064a\u0644";
        Salesman = "\u0628\u0627\u0626\u0639";
        SubTotal = "\u0627\u0644\u0625\u062c\u0645\u0627\u0644\u0649";
        NetSales = "\u0635\u0627\u0641\u064a \u0627\u0644\u0645\u0628\u064a\u0639\u0627\u062a";
        InvoiceDiscount = "\u062e\u0635\u0645 \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        OtherDiscount = "\u062e\u0635\u0645 \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        LineDiscount = "\u062e\u0635\u0645 \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        TCcharged = "TC \u0627\u062a\u0647\u0645";
        OriginalCopy = "\u0646\u0633\u062e\u0629 \u0646\u0647\u0627\u0626\u064a\u0629";
        DuplicateCopy = "\u0646\u0633\u062e\u0629 \u0645\u0643\u0631\u0631\u0629";
        DraftCopy = "\u0646\u0633\u062e\u0629 \u063a\u064a\u0631 \u0646\u0647\u0627\u0626\u064a\u0629";
        headerArabic = "*\u0627\u0644\u0635\u0646\u0641  \u0627\u0644\u0635\u0646\u0641                            \u0627\u0644\u0643\u0645\u064a\u0629   \u0627\u0644\u0628\u0627\u0643\u064a\u062a  \u0627\u0644\u062d\u0628\u0629\u0627\u0644\u062e\u0635\u0645    \u0627\u0644\u0642\u064a\u0645\u0629   !";
        headerrevereseArabic = "*   \u0627\u0644\u0642\u064a\u0645\u0629    \u0627\u0644\u062e\u0635\u0645  \u0627\u0644\u062d\u0628\u0629 \u0627\u0644\u0628\u0627\u0643\u064a\u062a  \u0627\u0644\u0643\u0645\u064a\u0629                           \u0627\u0644\u0635\u0646\u0641   \u0627\u0644\u0635\u0646\u0641!";
        headerbottomrevereseArabic = "*                \u0633\u0639\u0631    \u0633\u0639\u0631                                       \u0648\u0635\u0641     \u0631\u0642\u0645!";
        headerminirevereseArabic = "*\u0627\u0644\u0643\u0645\u064a\u0629                                                         \u0627\u0644\u0635\u0646\u0641      \u0627\u0644\u0635\u0646\u0641!";
        headerminibottomrevereseArabic = "*                                                                   \u0648\u0635\u0641        \u0631\u0642\u0645!";
        headerDotmatrevereseArabic = "*  \u0633\u0628\u0628     \u0627\u0644\u0642\u064a\u0645\u0629       \u0627\u0644\u062e\u0635\u0645        \u0627\u0644\u062d\u0628\u0629       \u0627\u0644\u0628\u0627\u0643\u064a\u062a         \u0627\u0644\u0643\u0645\u064a\u0629   \u0627\u0644\u0648\u062d\u062f\u0627\u062a                               \u0627\u0644\u0635\u0646\u0641        \u0627\u0644\u0635\u0646\u0641     .!";
        headerDotmatbottomrevereArab = "*                                      \u0633\u0639\u0631           \u0633\u0639\u0631                                                          \u0648\u0635\u0641          \u0631\u0642\u0645     .!";
        headerminiDotmatrevereseArabic = "*     \u0627\u0644\u0645\u062c\u0645\u0648\u0639       \u0627\u0644\u0643\u0645\u064a\u0629    \u0627\u0644\u0648\u062d\u062f\u0627\u062a                                                                              \u0627\u0644\u0635\u0646\u0641          \u0627\u0644\u0635\u0646\u0641    .!";
        headerminiDotmatbottomrevereArabic = "*                                                                                                                    \u0648\u0635\u0641            \u0631\u0642\u0645    .!";
        Cashinvoice = "\u0641\u0627\u062a\u0648\u0631\u0629\u0643\u0627\u0634";
        Creditinvoice = "\u0641\u0627\u062a\u0648\u0631\u0629\u0623\u062c\u0644";
        ROUTE = "\u0645\u0633\u0627\u0631";
        DATE = "\u062a\u0627\u0631\u064a\u062e";
        SALESMAN = "\u0628\u0627\u0626\u0639";
        TIME = "\u0645\u0631\u0629";
        Item = "\u0631\u0642\u0645 \u0627\u0644\u0635\u0646\u0641";
        OUTLET = "OUTLET CODE";
        DESCRIPTION = "\u0648\u0635\u0641 \u0627\u0644\u0635\u0646\u0641";
        UPC = "\u0627\u0644\u0648\u062d\u062f\u0627\u062a";
        QTY = "\u0627\u0644\u0643\u0645\u064a\u0629";
        TOTAL = "\u0627\u0644\u0645\u062c\u0645\u0648\u0639";
        CASEPRICE = "\u0633\u0639\u0631 \u0627\u0644\u0628\u0627\u0643\u064a\u062a";
        UNITPRICE = "\u0633\u0639\u0631 \u0627\u0644\u062d\u0628\u0629";
        DISCOUNT = "\u0627\u0644\u062e\u0635\u0645";
        AMOUNT = "\u0627\u0644\u0642\u064a\u0645\u0629";
        Receipt = "\u0633\u0646\u062f \u062a\u062d\u0635\u064a\u0644";
        PaymentDetails = "\u062a\u0641\u0627\u0635\u064a\u0644 \u0627\u0644\u0645\u062f\u0641\u0648\u0639\u0627\u062a";
        Total = "\u0627\u0644\u0625\u062c\u0645\u0627\u0644\u0649";
        Invoice = "\u0631\u0642\u0645 \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        InvoiceDate = "\u062a\u0627\u0631\u064a\u062e \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        InvoiceAmount = "\u0642\u064a\u0645\u0629 \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        AmountPaid = "\u0627\u0644\u0645\u0628\u0644\u063a \u0627\u0644\u0645\u062f\u0641\u0648\u0639";
        InvoiceBalance = "\u0631\u0635\u064a\u062f \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629";
        Cash = "\u0646\u0642\u062f\u0627";
    }

    public static String getCollectionHeader(boolean isHeader) {
        String header = "*\u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629          \u0627\u0644\u0645\u062f\u0641\u0648\u0639            \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629          \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629       \u0627\u0644\u0641\u0627\u062a\u0648\u0631\u0629           !";
        StringBuilder sbBottom = new StringBuilder();
        sbBottom.append("*");
        sbBottom.append("\u0631\u0635\u064a\u062f            ");
        sbBottom.append("\u0627\u0644\u0645\u0628\u0644\u063a          ");
        sbBottom.append("\u0642\u064a\u0645\u0629            ");
        sbBottom.append("\u062a\u0627\u0631\u064a\u062e           ");
        sbBottom.append("\u0631\u0642\u0645                 ");
        sbBottom.append("!");
        return isHeader ? header : sbBottom.toString();
    }

    public static String getHeaderVal(String HeaderVal) {
        if (HeaderVal.contains("CASE")) {
            return HeaderVal.replace("CASE", "OUTER");
        }
        if (HeaderVal.contains("UNIT")) {
            return HeaderVal.replace("UNIT", "PCS");
        }
        if (HeaderVal.contains("CAS")) {
            return HeaderVal.replace("CAS", "OUT");
        }
        return HeaderVal;
    }
}
