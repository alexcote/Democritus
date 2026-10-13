//Customizable Java buttons !! (up to 5)
//Copyright Daresbury Laboratory
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventObject;

public class DemButtons extends Applet
    implements ActionListener, MouseListener
{

    private URL destination;
    private String target1;
    private String target2;
    private String target3;
    private String target4;
    private String target5;
    private String author;
    private int numbutton;
    private GridLayout layout;
    private String caption1;
    private String caption2;
    private String caption3;
    private String caption4;
    private String caption5;
    private Button but1;
    private Button but2;
    private Button but3;
    private Button but4;
    private Button but5;
    private Font bfont;
    int butFontsize;
    int butFontstyle;
    String butFontname;
    Color backcolour;
    Color lettercolour;
    Color lettercolour2;
    Color backcolour2;

    public DemButtons()
    {
        numbutton = 0;
        but1 = new Button();
        but2 = new Button();
        but3 = new Button();
        but4 = new Button();
        but5 = new Button();
    }

    public void actionPerformed(ActionEvent ae)
    {
        Button button = (Button)ae.getSource();
        String s = button.getLabel();
        if(s == caption1)
            s = target1;
        if(s == caption2)
            s = target2;
        if(s == caption3)
            s = target3;
        if(s == caption4)
            s = target4;
        if(s == caption5)
            s = target5;
        gotourl(s);
    }

    public void gotourl(String s)
    {
        String s1 = getParameter("targetframe");
        if(s1 == null)
            s1 = "_self";
        System.out.println("Action");
        try
        {
            destination = new URL(getCodeBase(), s);
        }
        catch(MalformedURLException _ex)
        {
            System.out.println("Incorrect URL!");
        }
        getAppletContext().showDocument(destination, s1);
    }

    public void init()
    {
        if(getParameter("fontname") == null)
            butFontname = "TimesRoman";
        else
            butFontname = getParameter("fontname");

        if(getParameter("fontsize") == null)
            butFontsize = 12;
        else
            butFontsize = Integer.parseInt(getParameter("fontsize"));

        if(getParameter("bold") == null)
            butFontstyle = 0;
        else
        if(getParameter("bold").trim().toUpperCase().intern() == "YES".intern())
            butFontstyle = 1;

        if(getParameter("bcolour") == null)
            backcolour = getBackground();
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "R".intern())
            backcolour = Color.red;
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "B".intern())
            backcolour = Color.blue;
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "W".intern())
            backcolour = Color.white;
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "Y".intern())
            backcolour = Color.yellow;
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "G".intern())
            backcolour = Color.green;
        else
	    if(getParameter("bcolour").trim().toUpperCase().intern() == "H".intern())
            backcolour = Color.lightGray;
        else
        if(getParameter("bcolour").trim().toUpperCase().intern() == "P".intern()){
            Color purple = new Color(160,160,190);
            backcolour = purple;}

        if(getParameter("lcolour") == null)
            lettercolour = getForeground();
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "R".intern())
            lettercolour = Color.red;
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "B".intern())
            lettercolour = Color.blue;
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "W".intern())
            lettercolour = Color.white;
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "Y".intern())
            lettercolour = Color.yellow;
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "G".intern())
            lettercolour = Color.green;
        else
        if(getParameter("lcolour").trim().toUpperCase().intern() == "P".intern()){
            Color purple = new Color(160,160,190);
            lettercolour = purple;}

        if(getParameter("lcolour2") == null)
            lettercolour2 = lettercolour;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "R".intern())
            lettercolour2 = Color.red;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "B".intern())
            lettercolour2 = Color.blue;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "W".intern())
            lettercolour2 = Color.white;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "Y".intern())
            lettercolour2 = Color.yellow;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "G".intern())
            lettercolour2 = Color.green;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "L".intern())
            lettercolour2 = Color.black;
        else
        if(getParameter("lcolour2").trim().toUpperCase().intern() == "P".intern()){
            Color purple = new Color(160,160,190);
            lettercolour2 = purple;}

        if(getParameter("bcolour2") == null)
            backcolour2 = backcolour;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "R".intern())
            backcolour2 = Color.red;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "B".intern())
            backcolour2 = Color.blue;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "W".intern())
            backcolour2 = Color.white;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "Y".intern())
            backcolour2 = Color.yellow;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "G".intern())
            backcolour2 = Color.green;
        else
        if(getParameter("bcolour2").trim().toUpperCase().intern() == "P".intern()){
            Color purple = new Color(160,160,190);
            backcolour2 = purple;}

        bfont = new Font(butFontname, butFontstyle, butFontsize);
        author = getParameter("author");
        setVisible(true);
        if(author.trim().toUpperCase().intern() == "ALEX CÔTÉ")
        {
            caption1 = getParameter("caption1");
            caption2 = getParameter("caption2");
            caption3 = getParameter("caption3");
            caption4 = getParameter("caption4");
            caption5 = getParameter("caption5");
            if(caption1 != null)
                numbutton++;
            if(caption2 != null)
                numbutton++;
            if(caption3 != null)
                numbutton++;
            if(caption4 != null)
                numbutton++;
            if(caption5 != null)
                numbutton++;
            String s = getParameter("pos");
            if(s != null)
            {
                if(s.charAt(0) == "V".charAt(0))
                    layout = new GridLayout(numbutton, 1);
                if(s.charAt(0) == "H".charAt(0))
                    layout = new GridLayout(1, numbutton);
            } else
            {
                layout = new GridLayout(numbutton, 1);
            }
            setLayout(layout);
            if(caption1 != null)
            {
                add(but1);
                but1.setLabel(caption1);
                but1.setBackground(backcolour);
                but1.setForeground(lettercolour);
                but1.setFont(bfont);
                but1.addMouseListener(this);
                but1.addActionListener(this);
                target1 = getParameter("targ1");
            }
            if(caption2 != null)
            {
                add(but2);
                but2.setLabel(caption2);
                but2.setBackground(backcolour);
                but2.setForeground(lettercolour);
                but2.setFont(bfont);
                but2.addActionListener(this);
                but2.addMouseListener(this);
                target2 = getParameter("targ2");
            }
            if(caption3 != null)
            {
                add(but3);
                but3.setLabel(caption3);
                but3.setBackground(backcolour);
                but3.setForeground(lettercolour);
                but3.setFont(bfont);
                but3.addActionListener(this);
                but3.addMouseListener(this);
                target3 = getParameter("targ3");
            }
            if(caption4 != null)
            {
                add(but4);
                but4.setLabel(caption4);
                but4.setBackground(backcolour);
                but4.setForeground(lettercolour);
                but4.setFont(bfont);
                but4.addActionListener(this);
                but4.addMouseListener(this);
                target4 = getParameter("targ4");
            }
            if(caption5 != null)
            {
                add(but5);
                but5.setLabel(caption5);
                but5.setBackground(backcolour);
                but5.setForeground(lettercolour);
                but5.setFont(bfont);
                but5.addActionListener(this);
                but5.addMouseListener(this);
                target5 = getParameter("targ5");
            }
        }
    }

    public void mouseClicked(MouseEvent me)
    {
    }

    public void mouseEntered(MouseEvent me)
    {
        Button button = (Button)me.getSource();
        button.setForeground(lettercolour2);
        button.setBackground(backcolour2);
        String s = button.getLabel();
        button.setLabel(s);
    }

    public void mouseExited(MouseEvent me)
    {
        Button button = (Button)me.getSource();
        button.setForeground(lettercolour);
        button.setBackground(backcolour);
        String s = button.getLabel();
        button.setLabel(s);
    }

    public void mousePressed(MouseEvent me)
    {
    }

    public void mouseReleased(MouseEvent me)
    {
    }

}
