import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
//Copyright Daresbury Laboratory
class GraphRDF extends Frame
{
    Democritus home;
    Simulate dynamics;
    Image buffer;
    Graphics screengc;
    private int i, j, k, w, h, labstep;
    private double[] r,V,rsc,Vsc;
    private double m, end, C, Ar, pi, yax;
    private double maxvalr, minvalr, maxvalV, minvalV;
    private double xdim, ydim, sclx, scly, ystep;
    //boolean axe;
    private int count=1;
    private final int num = 128;
    private final int numpoints=6;


    Font f = new Font("Helvetica",Font.BOLD,12);

    GraphRDF(String title, Democritus here, Simulate motion)
    {
	super(title);
	home=here;
	dynamics=motion;
	pi=4.0*(Math.atan(1.0));
	m=0.0;
	k=0;

	r = new double[num];
	V = new double[num];
	rsc = new double[num];
	Vsc = new double[num];

	for(i=0;i<num;i++){
	    r[i]=0.0;
	    V[i]=0.0;
	}

	C=dynamics.counter*((dynamics.natms-1)/2.0);

	for (i=0;i<num;i++)
	    {
		r[i]=(i+0.5)*(dynamics.dr);
		Ar=2.0*pi*dynamics.dr*r[i];
		V[i]/=(dynamics.den*C*Ar);
	    }

	addWindowListener(new GraphWindowAdapter());
	addComponentListener(new GraphResizeAdapter());
    }


    double getxScale(){
	minvalr=0.0;
	maxvalr=0.0;
	for(i=0;i<num;i++)
	    {
		maxvalr=Math.max(maxvalr,r[i]);
		minvalr=Math.min(minvalr,r[i]);
	    }
	xdim=Math.abs(maxvalr-minvalr);
	sclx = w*0.80/xdim;
	return sclx;
    }

    double getyScale(){
	maxvalV=0.0;
	minvalV=0.0;
	for(i=0;i<num;i++)
	    {
		maxvalV=Math.max(maxvalV,V[i]);
		minvalV=Math.min(minvalV,V[i]);
	    }
	ydim=Math.abs(maxvalV-minvalV);
	scly = h*0.65/ydim;
	return scly;
    }

    public void paint(Graphics g){
	Dimension d = getSize();
	w = d.width;
	h = d.height;

	buffer=createImage(w,h);
	screengc=buffer.getGraphics();

	C=dynamics.counter*((dynamics.natms-1)/2.0);

	for (i=0;i<num;i++)
	    {
		r[i]=(i+0.5)*(dynamics.dr);
		Ar=2.0*pi*dynamics.dr*r[i];
		V[i]/=(dynamics.den*C*Ar);
	    }

	for(i=0;i<num;i++)
	    {
		rsc[i]=r[i];
		Vsc[i]=V[i];
	    }

	// Scaling the graph for the screen
	if(count<50 || count%120==0){
	    getxScale();
	    getyScale();
	}
	count++;


	for(i=0;i<num;i++)
	    {
		rsc[i]*=sclx;
		Vsc[i]*=scly;
	    }

	screengc.setColor(Color.red);
	for(i=0;i<(num-1);i++)
	    screengc.drawLine((int)(rsc[i]+0.08*w),(int)(h-(Vsc[i]+0.11*h)),(int)(rsc[i+1]+0.08*w),(int)(h-(Vsc[i+1]+0.11*h)));

	screengc.setColor(Color.black);
	screengc.drawRect((int)(0.08*w),(int)(0.07*h),(int)(w-0.16*w),(int)(h-0.18*h));
	screengc.setFont(f);

	screengc.drawString("Radial Distribution Function", (int)(0.35*w), (int)(0.11*h));
	screengc.drawString("g(r) vs. r", (int)(0.43*w), (int)(0.14*h));
	if(count<50){
		screengc.drawString("g(r)", (int)(0.03*w), (int)(0.5*h));
		screengc.drawString("r", (int)(w-0.1*w),(int)(h-0.08*h));
	} else {
		ystep=0.75*h/numpoints;
		labstep=(int)(ystep/scly);
		if ( labstep == 0 ) labstep = 1;
		for(i=0; i<=(numpoints+1); i++)
			{
				DecimalFormat df = new DecimalFormat( "0.0" );
				String l = df.format( i*labstep );
				//String l = Integer.toString( i*labstep )+".0";
				int twidth = (int)(screengc.getFontMetrics().getStringBounds( l, screengc ).getWidth());
				if((int)(h-(i*labstep*scly+0.11*h)+0.01*h)>(int)(0.07*h))
					screengc.drawString( l, (int)(0.075*w - twidth), (int)(h-(i*labstep*scly+0.11*h)+0.01*h));
			}
	    /*axe=true;
	    yax=1.0;
	   	while(axe) {
	    	if((int)(h-(yax*scly+0.11*h)+0.01*h)>(int)(0.07*h)){
	    		screengc.drawString(Double.toString(yax),(int)(0.04*w),(int)(h-(yax*scly+0.11*h)+0.01*h));
	   			yax++;
	   		} else {
				axe=false;
				yax=1.0;
			}
		}*/
	    screengc.drawString((Double.toString(r[31]).substring(0,3)),(int)(rsc[31]+0.08*w-10),(int)(h-0.08*h));
	    screengc.drawString((Double.toString(r[63]).substring(0,3)),(int)(rsc[63]+0.08*w-10),(int)(h-0.08*h));
	    screengc.drawString((Double.toString(r[95]).substring(0,3)),(int)(rsc[95]+0.08*w-10),(int)(h-0.08*h));
	    screengc.drawString((Double.toString(r[127]).substring(0,3)),(int)(rsc[127]+0.08*w-10),(int)(h-0.08*h));
	}
	g.drawImage(buffer,0,0,this);
    }

    public void update(Graphics g){
	paint(g);
    }

    public void update(){
	for(i=0;i<num;i++)
	    V[i]=dynamics.gor[i];
	repaint();
    }

    class GraphWindowAdapter extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
	    home.setEnabled(true);
	    home.qn=false;
	    setVisible(false);
	    screengc.dispose();
	}
    }

    class GraphResizeAdapter extends ComponentAdapter {
	public void componentResized(ComponentEvent ce){
	    count=1;
	    repaint();
	}
    }

}











