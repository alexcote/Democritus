import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
//Copyright Daresbury Laboratory
class GraphMSD extends Frame
{
    Democritus home;
    Simulate dynamics;
    Image buffer;
    Graphics screengc;
    private int i, j, k, w, h, m, count;
    private double maxvalt, minvalt, maxvalR, minvalR;
    private double sclx, scly, xdim, ydim, ystep, labstep;
    private double[] t, R, tsc, Rsc;
    private final int num=250;
    private final int numpoints=6;
    Font f = new Font("Helvetica",Font.BOLD,12);
    Font ff = new Font("Helvetica",Font.BOLD,14);

   GraphMSD(String title, Democritus here, Simulate motion)
    {
	super(title);
	home=here;
	dynamics=motion;
	t = new double[num];
	R = new double[num];
	tsc = new double[num];
	Rsc = new double[num];
	count=1;

	addWindowListener(new GraphWindowAdapter());
    }


    public void paint(Graphics g){
	Dimension d = getSize();
	w = d.width;
	h = d.height;
	buffer=createImage(w,h);
	screengc=buffer.getGraphics();

	m=0;
	maxvalR=0.0;
	minvalR=0.0;
	minvalt=0.0;
	maxvalt=0.0;

	if(count==1){
	    for(i=0;i<num;i++)
		R[i]=0.0;
	    screengc.setColor(Color.blue);
	    screengc.setFont(ff);
	    screengc.drawString("Please wait for the MSD to build up", (int)(0.25*w), (int)(0.45*h));
	    screengc.setColor(Color.black);
	}

	for (i=0;i<num;i++)
	    {
		m+=10;
		t[i]=m;
		R[i]=dynamics.TR2[i]/count;
	    }

	for(i=0;i<num;i++)
	    {
		maxvalt=Math.max(maxvalt,t[i]);
		minvalt=Math.min(minvalt,t[i]);
		maxvalR=Math.max(maxvalR,R[i]);
		minvalR=Math.min(minvalR,R[i]);
	    }

	for(i=0;i<num;i++)
	    {
		tsc[i]=t[i];
		Rsc[i]=R[i];
	    }

	xdim=Math.abs(maxvalt-minvalt);
	ydim=Math.abs(maxvalR-minvalR);
	sclx = w*0.80/xdim;
	scly = h*0.65/ydim;

	// Scaling the graph for the screen
	for(i=0;i<num;i++)
	    {
		tsc[i]*=sclx;
		Rsc[i]*=scly;
            }
	screengc.drawRect((int)(0.09*w),(int)(0.07*h),(int)(w-0.15*w),(int)(h-0.18*h));
	screengc.setFont(f);

	screengc.drawString("Mean Square Displacement",(int)(0.35*w), (int)(0.11*h));
	screengc.drawString("R"+(char)178+" vs. t",(int)(0.43*w), (int)(0.14*h));

	if(count>1){
	    screengc.setColor(Color.red);
	    for(i=0;i<(num-1);i++)
	    screengc.drawLine((int)(tsc[i]+0.09*w),(int)(h-(Rsc[i]+0.11*h)),(int)(tsc[i+1]+0.09*w),(int)(h-(Rsc[i+1]+0.11*h)));
	    screengc.setColor(Color.black);

	    ystep=0.75*h/numpoints;
		labstep=(ystep/scly);

		for(i=0; i<=(numpoints+1); i++)
	 		{
				DecimalFormat df = new DecimalFormat( "0.000" );
				String l = df.format( i*labstep );
				int twidth = (int)(screengc.getFontMetrics().getStringBounds( l, screengc ).getWidth());
				if((int)(h-(i*labstep*scly+0.11*h)+0.01*h)>(int)(0.07*h))
				screengc.drawString( l, (int)(0.085*w - twidth), (int)(h-(i*labstep*scly+0.11*h)+0.01*h));
			}
		screengc.drawString(Double.toString(t[49]).substring(0,3),(int)(tsc[49]+0.09*w-20),(int)(h-0.08*h));
	  	screengc.drawString(Double.toString(t[99]).substring(0,4),(int)(tsc[99]+0.09*w-20),(int)(h-0.08*h));
		screengc.drawString(Double.toString(t[149]).substring(0,4),(int)(tsc[149]+0.09*w-20),(int)(h-0.08*h));
	  	screengc.drawString(Double.toString(t[199]).substring(0,4),(int)(tsc[199]+0.09*w-20),(int)(h-0.08*h));
	  	screengc.drawString(Double.toString(t[249]).substring(0,4),(int)(tsc[249]+0.09*w-20),(int)(h-0.08*h));
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
	    home.qm=false;
	    setVisible(false);
	    screengc.dispose();
	}
    }
}











