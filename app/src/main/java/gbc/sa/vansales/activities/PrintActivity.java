package gbc.sa.vansales.activities;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Muhammad Umair on 11-01-2017.
 */

public class PrintActivity extends AppCompatActivity
{
    /*// Set PDF document Properties
    public void addMetaData(Document document)
    {
        document.addTitle("Title");
        document.addSubject("Subject");
        document.addKeywords("Items, Cases, Units, Price");
        document.addAuthor("TAG");
        document.addCreator("TallyMarks Consulting");
    }

    public Font categoryFont()
    {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        return catFont;
    }

    public Font smallNormalFont()
    {
        Font smallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        return smallNormal;
    }

    public Font normalFont()
    {
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        return normal;
    }

    public Font tabelRowFont()
    {
        Font tableRow= new Font(Font.FontFamily.TIMES_ROMAN, 8);
        return tableRow;
    }

    public Font tableRowHeadingFont()
    {
        Font tableRowHeading = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(0, 0, 0));
        return  tableRowHeading;
    }

    public String currentDate()
    {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return date;
    }
    public String currentTime()
    {
        String time= new SimpleDateFormat("hh:mm a").format(new Date());
        return time;
    }
    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderColor(BaseColor.WHITE);
        if(text.trim().equalsIgnoreCase(""))
        {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }

    public void insertBorderedCell(PdfPTable table, String text, int align, int colspan, Font font)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if(text.trim().equalsIgnoreCase(""))
        {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }*/
}
