import java.awt.*;
import java.awt.event.*;
import java.applet.*;
//Copyright Daresbury Laboratory
public class Democritus extends Applet implements AdjustmentListener, ActionListener
{

    MolDyn picture;
    Simulate dynamics;
    Scrollbar adjden,adjtem;
    private int tile,tilex,tiley,wd,ht;
    private double den,tem;
    private double fntscl;
    TextField tname1,tname2,tname3,tname4;
    protected boolean qn, qm, qt;
    GraphRDF grdf;
    GraphMSD gmsd;
    GraphVAC gvac;

    public void init()
    {
	qn = false;
	qm = false;
	qt = false;
	//	set screen characteristics

	String screen = getParameter("WIDTH");
	String grwidth = getParameter("wd");
	String grheight = getParameter("ht");
        tile=Integer.parseInt(screen);
	tilex=tile/4;
	tiley=tile/20;
	wd=Integer.parseInt(grwidth);
	ht=Integer.parseInt(grheight);
	Color purple = new Color(160,160,190);
	setBackground(purple);

	fntscl=(double)tile/350.0;  // scaling the fonts

	setLayout(new BorderLayout());
	//     Main screen
	Panel panel1 = new Panel();
	picture = new MolDyn();
	dynamics = new Simulate();
	panel1.add(picture);

	//     Information and adjustment part
	Panel panel2 = new Panel();
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints constraints = new GridBagConstraints();
	panel2.setLayout(gridbag);

	constraints.fill=GridBagConstraints.BOTH;


	//     	Label 1 - density

	buildConstraints(constraints,0,0,1,1,tilex,tiley);
	Font f = new Font("Helvetica", Font.BOLD,(int)(17*fntscl));
	Label label1 = new Label("  Density  ",Label.CENTER);
	label1.setFont(f);
	gridbag.setConstraints(label1,constraints);
	panel2.add(label1);

	//     	Label 2 - temperature

	buildConstraints(constraints,1,0,1,1,tilex,tiley);
	Label label2 = new Label("Temperature",Label.CENTER);
	label2.setFont(f);
	gridbag.setConstraints(label2,constraints);
	panel2.add(label2);

	//     	Label 3 - pressure

	buildConstraints(constraints,2,0,1,1,tilex,tiley);
	Label label3 = new Label("  Pressure ",Label.CENTER);
	label3.setFont(f);
	gridbag.setConstraints(label3,constraints);
	panel2.add(label3);

	//     	Label 4 - energy

	buildConstraints(constraints,3,0,1,1,tilex,tiley);
	Label label4 = new Label("  Energy   ",Label.CENTER);
	label4.setFont(f);
	gridbag.setConstraints(label4,constraints);
	panel2.add(label4);

	//     	Text field 1 - density


	Font ff=new Font("Courier", Font.PLAIN, (int)(18*fntscl));
	buildConstraints(constraints,0,1,1,1,tilex,tiley);
	tname1 = new TextField();
	tname1.setFont(ff);
	tname1.setEditable(false);
	gridbag.setConstraints(tname1,constraints);
	panel2.add(tname1);

	//     	Text field 2 - temperature

	buildConstraints(constraints,1,1,1,1,tilex,tiley);
	tname2 = new TextField();
	tname2.setFont(ff);
	tname2.setEditable(false);
	gridbag.setConstraints(tname2,constraints);
	panel2.add(tname2);

	//     	Text field 3 - pressure

	buildConstraints(constraints,2,1,1,1,tilex,tiley);
	tname3 = new TextField();
	tname3.setFont(ff);
	tname3.setEditable(false);
	gridbag.setConstraints(tname3,constraints);
	panel2.add(tname3);

	//     	Text field 4 - energy

	buildConstraints(constraints,3,1,1,1,tilex,tiley);
	tname4 = new TextField();
	tname4.setFont(ff);
	tname4.setEditable(false);
	gridbag.setConstraints(tname4,constraints);
	panel2.add(tname4);

	//     	Label 5 - density adjustment

	buildConstraints(constraints,0,2,1,1,tilex,tiley);
	Font fff = new Font("Helvetica", Font.BOLD,(int)(16*fntscl));
	Label label5 = new Label("Den <->",Label.CENTER);
	label5.setFont(fff);
	gridbag.setConstraints(label5,constraints);
	panel2.add(label5);

	//     	Label 6 - temperature adjustment

	buildConstraints(constraints,0,3,1,1,tilex,tiley);
	Label label6 = new Label("Tem <->",Label.CENTER);
	label6.setFont(fff);
	gridbag.setConstraints(label6,constraints);
	panel2.add(label6);

	//     	Scrollbar 1 - density adjustor

	buildConstraints(constraints,1,2,3,1,3*tilex,tiley);
	constraints.fill=GridBagConstraints.HORIZONTAL;
	adjden = new Scrollbar(Scrollbar.HORIZONTAL,500,15,60,1015);
	adjden.setUnitIncrement(10);
	adjden.setBlockIncrement(100);
	gridbag.setConstraints(adjden,constraints);
	panel2.add(adjden);
	den=(double)adjden.getValue()/1000.0;

	//     	Scrollbar 2 - temperature adjustor

	buildConstraints(constraints,1,3,3,1,3*tilex,tiley);
	adjtem = new Scrollbar(Scrollbar.HORIZONTAL,500,15,10,1015);
	adjtem.setUnitIncrement(10);
	adjtem.setBlockIncrement(100);
	gridbag.setConstraints(adjtem,constraints);
	panel2.add(adjtem);
	tem=(double)adjtem.getValue();

	picture.setMolDyn(this,dynamics,tile);

	picture.update(den,tem);
	dynamics.update(den,tem);

	//     register to receive adjustment events

	adjtem.addAdjustmentListener(this);
	adjden.addAdjustmentListener(this);

	Panel panel3 = new Panel();
	panel3.setLayout(new FlowLayout());
	Button but1 = new Button("RDF");
        but1.setFont(f);
	but1.addActionListener(this);
	Button but2 = new Button("MSD");
	but2.setFont(f);
	but2.addActionListener(this);
	Button but3 = new Button("VAC");
	but3.setFont(f);
	but3.addActionListener(this);
	panel3.add(but1);
	panel3.add(but2);
	panel3.add(but3);

	add(panel1, BorderLayout.NORTH);
	add(panel2, BorderLayout.CENTER);
	add(panel3, BorderLayout.SOUTH);
    }


    public void start()
    {
	// Start molecular dynamics
	picture.start();
    }


    public void stop()
    {
	// Stop molecular dynamics
	this.setEnabled(true);
	if(grdf!=null){
	    grdf.setVisible(false);
	    grdf.screengc.dispose();
	    grdf=null;
	    dynamics.update(grdf);
	}
	if(gmsd!=null){
	    gmsd.setVisible(false);
	    gmsd.screengc.dispose();
	    gmsd=null;
	    dynamics.update2(gmsd);
	}
	if(gvac!=null){
	    gvac.setVisible(false);
	    gvac.screengc.dispose();
	    gvac=null;
	    dynamics.update3(gvac);
	}

	picture.stop();
    }

    public void destroy()
    {
	// Destroy the applet
	picture.destroy();
    }


    void buildConstraints(GridBagConstraints gbc,int gx, int gy,
			  int gw, int gh, int wx, int wy)
    {
	gbc.gridx=gx;
	gbc.gridy=gy;
	gbc.gridwidth=gw;
	gbc.gridheight=gh;
	gbc.weightx=wx;
	gbc.weighty=wy;
    }

    void update()
    {
	tname1.setText(String.valueOf(dynamics.den));
	String tex2 = String.valueOf(dynamics.tmpav);
	int fld2 = Math.min(tex2.length(),8);
	tname2.setText(tex2.substring(0,fld2));
	String tex3 = String.valueOf(dynamics.prsav);
	int fld3 = Math.min(tex3.length(),7);
	tname3.setText(tex3.substring(0,fld3));
	String tex4 = String.valueOf(dynamics.totav);
	int fld4 = Math.min(tex4.length(),6);
	tname4.setText(tex4.substring(0,fld4));
    }

    public void actionPerformed(ActionEvent ae)
    {
	String str = ae.getActionCommand();
	if(str.equals("RDF")){
	    grdf = new GraphRDF("Graph Window",this, dynamics);
	    grdf.setSize(500,450);
	    grdf.setLocation((int)(wd/2.66),(int)(ht/5.45));
	    grdf.setVisible(true);
	    this.setEnabled(false);
	    qn=true;
	    dynamics.update(grdf);
	}
	else if(str.equals("MSD")){
	    gmsd = new GraphMSD("Graph Window", this, dynamics);
	    gmsd.setSize(500,450);
	    gmsd.setLocation((int)(wd/2.66),(int)(ht/5.45));
	    gmsd.setVisible(true);
	    this.setEnabled(false);
	    qm=true;
	    dynamics.update2(gmsd);
	}
	else{
	    gvac = new GraphVAC("Graph Window", this, dynamics);
	    gvac.setSize(500,450);
	    gvac.setLocation((int)(wd/2.66),(int)(ht/5.45));
	    gvac.setVisible(true);
	    this.setEnabled(false);
	    qt=true;
	    dynamics.update3(gvac);
	}
    }

    public void adjustmentValueChanged(AdjustmentEvent ae)
    {
	den=(double)adjden.getValue()/1000.0;
	tem=(double)adjtem.getValue();
	picture.update(den,tem);
	dynamics.update(den,tem);
    }

}

