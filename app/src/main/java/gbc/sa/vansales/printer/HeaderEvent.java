package gbc.sa.vansales.printer;
import java.util.EventObject;
/**
 * Created by Rakshit on 06-Feb-17.
 */
public class HeaderEvent extends EventObject {
    static final long serialVersionUID = 1;

    public HeaderEvent(LinePrinter linePrinter) {
        super(linePrinter);
    }
}
