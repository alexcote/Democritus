import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
//Copyright Daresbury Laboratory
class GraphVAC extends Frame
{
    Democritus home;
    Simulate dynamics;
    Image buffer;
    Graphics screengc;
    private int i, j, k, w, h, m, count;
    private double sclx, scly, xdim, ydim, ystep;//, labstep;
    private double[] t, v, tsc, vsc;
    private int cntax=1;
    private final int num=250;
    //final int numpoints=6;
    Font f = new Font("Helvetica",Font.BOLD,12);
    Font ff = new Font("Helvetica",Font.BOLD,14);

    GraphVAC(String title, Democritus here, Simulate motion)
    {
	super(title);
	home=here;
	dynamics=motion;
	t = new double[num];
	v = new double[num];
	tsc = new double[num];
	vsc = new double[num];
	count=0;
	addWindowListener(new GraphWindowAdapter());
	addComponentListener(new GraphResizeAdapter());
    }

    double getxScale(){
		xdim=(double)num;
	    sclx = w*0.80/xdim;
		return sclx;
	}

	double getyScale(){
		ydim=1.15;
		scly = h*0.50/ydim;
		return scly;
	}

    public void paint(Graphics g){
	Dimension d = getSize();
	w = d.width;
	h = d.height;
	buffer=createImage(w,h);
	screengc=buffer.getGraphics();

	m=0;
	if(count==1){
	    for(i=0;i<num;i++)
		v[i]=0.0;
	    screengc.setColor(Color.blue);
	    screengc.setFont(ff);
	    screengc.drawString("Please wait for the VAC to build up", (int)(0.25*w), (int)(0.45*h));
	    screengc.setColor(Color.black);
	}


	if(count>1){
	for (i=0;i<num;i++)
	    {
		m+=1;
		t[i]=m;
		v[i]=((count-2)*v[i]/(count-1))+(dynamics.vac2[i]/(dynamics.vac2[0]*(count-1)));
		//if(count==100)
		//System.out.println(t[i]+"    "+v[i]);
	    }



	for(i=0;i<num;i++)
	    {
		tsc[i]=t[i];
		vsc[i]=v[i];
	    }


	// Scaling the graph for the screen
	if(cntax<2){
		getxScale();
		getyScale();
		cntax++;
	}

	for(i=0;i<num;i++)
	    {
		tsc[i]*=sclx;
		vsc[i]*=scly;
        }

    }
	screengc.drawRect((int)(0.08*w),(int)(0.07*h),(int)(w-0.16*w),(int)(h-0.18*h));
	screengc.setFont(f);

	screengc.drawString("Velocity autocorrelation function", (int)(0.35*w), (int)(0.11*h));
	screengc.drawString("plotted against time", (int)(0.43*w), (int)(0.14*h));

	if(count>1){
	    screengc.setColor(Color.red);
	    for(i=0;i<(num-1);i++)
			screengc.drawLine((int)(tsc[i]+0.08*w),(int)(h-(vsc[i]+0.35*h)),(int)(tsc[i+1]+0.08*w),(int)(h-(vsc[i+1]+0.35*h)));
	    screengc.setColor(Color.black);

	   /* ystep=0.75*h/numpoints;
		labstep=ystep/scly;
	    for(i=0; i<=(numpoints+1); i++)
			 {
				DecimalFormat df = new DecimalFormat( "0.00" );
				String l = df.format( i*labstep );
				int twidth = (int)(screengc.getFontMetrics().getStringBounds( l, screengc ).getWidth());
				if((int)(h-(i*labstep*scly+0.35*h)+0.01*h)>(int)(0.07*h))
					screengc.drawString( l, (int)(0.075*w - twidth), (int)(h-(i*labstep*scly+0.35*h)+0.01*h));
			}*/
	   	screengc.drawString("1.0",(int)(0.04*w), (int)(h-(scly+0.34*h)));
	   	screengc.drawString("0.75",(int)(0.04*w-7), (int)(h-(0.75*scly+0.34*h)));
	   	screengc.drawString("0.5",(int)(0.04*w), (int)(h-(0.5*scly+0.34*h)));
	   	screengc.drawString("0.25",(int)(0.04*w-7), (int)(h-(0.25*scly+0.34*h)));
	    screengc.drawLine((int)(0.08*w),(int)(h-0.35*h),(int)(w-0.08*w),(int)(h-0.35*h));
	    screengc.drawString("0.0",(int)(0.04*w),(int)(h-0.34*h));
	   	screengc.drawString("-0.25",(int)(0.04*w-11), (int)(h+0.25*scly-0.34*h));
	   	screengc.drawString("-0.5",(int)(0.04*w-4), (int)(h+0.5*scly-0.34*h));
	    screengc.drawString("50",(int)(tsc[49]+0.08*w-10),(int)(h-0.32*h));
	    screengc.drawString("100",(int)(tsc[99]+0.08*w-10),(int)(h-0.32*h));
	    screengc.drawString("150",(int)(tsc[149]+0.08*w-10),(int)(h-0.32*h));
	    screengc.drawString("200",(int)(tsc[199]+0.08*w-10),(int)(h-0.32*h));
	    screengc.drawString("250",(int)(tsc[249]+0.08*w-10),(int)(h-0.32*h));
	    //screengc.drawString("t ->",(int)(w-0.3*w),(int)(h-0.29*h));
	}
	g.drawImage(buffer,0,0,this);
	count++;
    }

    public void update(Graphics g){
	paint(g);
    }

    public void update(){
	repaint();
    }

    class GraphWindowAdapter extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
	    home.setEnabled(true);
	    home.qt=false;
	    setVisible(false);
	    screengc.dispose();
	}
    }

    class GraphResizeAdapter extends ComponentAdapter {
	public void componentResized(ComponentEvent ce) {
		cntax=1;
		repaint();
	}
	}
}
