package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.adapters.PrintDocumentAdapter;
import gbc.sa.vansales.google.Location;
import gbc.sa.vansales.utils.Callback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
public class InformationsActivity extends AppCompatActivity {

    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {};/*{"CUSTOMER LIST", "ITEM LIST", "TARGET/GOALS",
            "ANALYSIS", "TODAY SUMMARY", "REVIEW", "PRINT \n TRANSACTIONS", "PRINT REPORTS"};*/
    int resarr[] = {R.drawable.info_customer_list, R.drawable.info_item_list, R.drawable.info_target,
            R.drawable.info_analysis, R.drawable.info_todays_summery, R.drawable.info_review,
            R.drawable.info_print_transaction, R.drawable.info_print_reports};

    ImageView iv_back;
    TextView tv_top_header;
    View view1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        strText = new String[]{getString(R.string.customer_list), getString(R.string.item_list), getString(R.string.targetgoal),
                getString(R.string.analysis), getString(R.string.today_summary), getString(R.string.review),
                getString(R.string.printtransactions), getString(R.string.printreports)};
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid_information);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.information));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new CustomerOperationAdapter(InformationsActivity.this, strText, resarr, "InformationsActivity");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view1 = view;

                switch (position) {
                    case 0:
                        Intent customerlist = new Intent(InformationsActivity.this,CustomerListActivity.class);
                        startActivity(customerlist);
                        break;
                    case 1:
                        Intent itemlist = new Intent(InformationsActivity.this,ItemListActivity.class);
                        startActivity(itemlist);
                        break;
                    case 2:
                        /*new Location(InformationsActivity.this, new Callback() {
                            @Override
                            public void callbackSuccess(android.location.Location location) {
                                Log.e("COORDUNATES",String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                                Toast.makeText(getBaseContext(),String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()),Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void callbackFailure() {
                                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        /*try {
                            generateInvoicePDF();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }*/
                        break;
                    case 3:

                        break;
                    case 4:
                        Intent todays = new Intent(InformationsActivity.this,TodaysSummaryActivity.class);
                        startActivity(todays);
                        break;
                    case 5:
                        Intent review = new Intent(InformationsActivity.this,ReviewActivity.class);

                        startActivity(review);
                        break;
                    case 6:
                        Intent printdoc= new Intent(InformationsActivity.this,PrintDocumentActivity.class);
                        startActivity(printdoc);

                        break;
                    case 7:
                        Intent print = new Intent(InformationsActivity.this,PrinterReportsActivity.class);
                        print.putExtra("from","info");
                        startActivity(print);

                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*private void generateInvoicePDF() throws DocumentException {
        PrintActivity printActivityObject = new PrintActivity();
        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/PDF/" + "Invoice.pdf";
        Document document = new Document(PageSize.A4);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PDF");
        double left=1.27,right=1.27,top=3.81,bottom=5.08; // page margins in cm's
        if (!myDir.exists())
            myDir.mkdir();
        try
        {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            document.setMargins(((float) left),((float) right) ,((float) top),((float) bottom));
            printActivityObject.addMetaData(document);
            addTitlePage(document,printActivityObject);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        document.close();
        Toast.makeText(this, "PDF Generated : Local Storage/DownLoads/PDF/",
                Toast.LENGTH_LONG).show();
    }

    private void addTitlePage(Document document,PrintActivity printActivityObject) throws DocumentException
    {
        Paragraph prHead = new Paragraph();
        PdfPTable myTable = new PdfPTable(1);
        myTable.setWidthPercentage(100.0f);

        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        myCell.setBorder(Rectangle.BOTTOM);
        myTable.addCell(myCell);

        prHead.setFont(printActivityObject.categoryFont());
        prHead.add("Invoice ");

        prHead.add("\nDelievery for : ---");

        prHead.add("\nDelievery route :  ---");

        prHead.setAlignment(Element.ALIGN_CENTER);



        float[] columnWidthsRouteTable = {5f,5f, 8f, 6f, 5f,5f,8f,8f};
        PdfPTable table2 = new PdfPTable(columnWidthsRouteTable);
        table2.setWidthPercentage(90f);

        printActivityObject.insertCell(table2, "Route#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Salesman#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Salesman name", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Doc#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Sales date",Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Customer#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Customer name(EN)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table2, "Customer name(AR)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        table2.setHeaderRows(1);

        for(int x=1; x<2; x++)
        {

            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2,"---" , Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2,"---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table2, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
        }

        float[] columnWidths = {1.5f,4f, 4f, 2f, 2f,2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(90f);

        printActivityObject.insertCell(table, "Item#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table, "Description(EN)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table, "Description(AR)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table, "Total units", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table, "Unit price",Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(table, "Amount", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        table.setHeaderRows(1);



        for(int i=0; i<5; i++)
        {

            printActivityObject.insertCell(table,"---",Element.ALIGN_CENTER,1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table, "---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(table,"---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            if(i==4)
            {
                printActivityObject.insertCell(table, "", Element.ALIGN_CENTER, 4, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(table,"Total : ", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(table, "0.000", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
            }
        }






        float[] columnWidthsGoodReturns = {1.5f,4f, 4f, 2f, 2f,2f};
        PdfPTable tableGoodReturns = new PdfPTable(columnWidthsGoodReturns);
        tableGoodReturns.setWidthPercentage(90f);

        printActivityObject.insertCell(tableGoodReturns, "Item#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableGoodReturns, "Description(EN)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableGoodReturns, "Description(AR)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableGoodReturns, "Total units", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableGoodReturns, "Unit price",Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableGoodReturns, "Amount", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        tableGoodReturns.setHeaderRows(1);



        for(int i=0; i<5; i++)
        {

            printActivityObject.insertCell(tableGoodReturns,"---",Element.ALIGN_CENTER,1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableGoodReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableGoodReturns, "---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableGoodReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableGoodReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableGoodReturns,"---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            if(i==4)
            {
                printActivityObject.insertCell(tableGoodReturns, "", Element.ALIGN_CENTER, 4, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(tableGoodReturns,"Total : ", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(tableGoodReturns, "0.000", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
            }
        }



        float[] columnWidthsBadReturns = {1.5f,4f, 4f, 2f, 2f,2f};
        PdfPTable tableBadReturns = new PdfPTable(columnWidthsBadReturns);
        tableBadReturns.setWidthPercentage(90f);

        printActivityObject.insertCell(tableBadReturns, "Item#", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableBadReturns, "Description(EN)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableBadReturns, "Description(AR)", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableBadReturns, "Total units", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableBadReturns, "Unit price",Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        printActivityObject.insertCell(tableBadReturns, "Amount", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
        tableBadReturns.setHeaderRows(1);

        for(int i=0; i<5; i++)
        {

            printActivityObject.insertCell(tableBadReturns,"---",Element.ALIGN_CENTER,1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableBadReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableBadReturns, "---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableBadReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableBadReturns, "---", Element.ALIGN_CENTER, 1, printActivityObject.tabelRowFont());
            printActivityObject.insertCell(tableBadReturns,"---", Element.ALIGN_CENTER, 1,printActivityObject.tabelRowFont());
            if(i==4)
            {
                printActivityObject.insertCell(tableBadReturns, "", Element.ALIGN_CENTER, 4, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(tableBadReturns,"Total : ", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
                printActivityObject.insertCell(tableBadReturns, "0.000", Element.ALIGN_CENTER, 1, printActivityObject.tableRowHeadingFont());
            }
        }

        Paragraph prNewLine = new Paragraph();
        prNewLine.setFont(printActivityObject.smallNormalFont());
        prNewLine.add("\n");

        Paragraph prList = new Paragraph();
        prList.setFont(printActivityObject.smallNormalFont());
        prList.add("Sales : ---");
        prList.add("\nGood returns : ---");
        prList.add("\nBad returns : ---");
        prList.add("\nNet sales : ---");
        prList.add("\nNet sales due invoice : ---");

        Paragraph prTableHeading = new Paragraph();
        prTableHeading.setFont(printActivityObject.categoryFont());
        prTableHeading.add("\nSales\n");
        prTableHeading.setAlignment(Element.ALIGN_CENTER);

        Paragraph prTableHeadingGoodReturn = new Paragraph();
        prTableHeadingGoodReturn.setFont(printActivityObject.categoryFont());
        prTableHeadingGoodReturn.add("Good returns\n");
        prTableHeadingGoodReturn.setAlignment(Element.ALIGN_CENTER);

        Paragraph prTableHeadingBadReturns = new Paragraph();
        prTableHeadingBadReturns.setFont(printActivityObject.categoryFont());
        prTableHeadingBadReturns.add("Bad returns\n");
        prTableHeadingBadReturns.setAlignment(Element.ALIGN_CENTER);

        Paragraph prDate = new Paragraph();
        prDate.setFont(printActivityObject.normalFont());
        prDate.add("Date/Time");
        prDate.add(" : "+printActivityObject.currentDate()+" / "+printActivityObject.currentTime());
        prDate.setAlignment(Element.ALIGN_RIGHT);

        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph pSignature = new Paragraph("\nSalesman signature");
        pSignature.add(new Chunk(glue));
        pSignature.add("Customer signature");

        Chunk glue2 = new Chunk(new VerticalPositionMark());
        Paragraph pSignature2 = new Paragraph("\n---------------------");
        pSignature2.add(new Chunk(glue2));
        pSignature2.add("----------------------");

        Paragraph prPageNo = new Paragraph();
        prPageNo.setFont(printActivityObject.normalFont());
        prPageNo.setAlignment(Element.ALIGN_CENTER);
        document.add(prHead);
        document.add(prDate);
        document.add(table2);
        document.add(prTableHeading);
        document.add(prNewLine);
        document.add(table);
        document.add(prTableHeadingGoodReturn);
        document.add(prNewLine);
        document.add(tableGoodReturns);
        document.add(prTableHeadingBadReturns);
        document.add(prNewLine);
        document.add(tableBadReturns);
        document.add(prList);
        document.add(pSignature);
        document.add(pSignature2);
        document.newPage();
    }*/
}
