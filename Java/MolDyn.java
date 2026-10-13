import java.awt.*;
import java.awt.event.*;
//Copyright Daresbury Laboratory
public class MolDyn extends Canvas implements Runnable
{
    Thread runner;
    private boolean stopFlag;
    Democritus home;
    private int[] xpos,ypos;
    Simulate dynamics;
    private int nstep,matms,natms,tile,dia,hdi;
    private double tem,den,box,scrn;

    Image offscreenImg;
    Graphics offscreenG;
    private int mouseX, mouseY;
    private int ident=-1;


    public void setMolDyn(Democritus here, Simulate motion, int tilesize)
    {
	nstep=0;
	matms=5;
	den=0.5;
	home=here;

	tem=500.0;
	natms=matms*matms;
	tile=tilesize;

	xpos = new int[natms];
	ypos = new int[natms];

	dynamics = motion;
	dynamics.setParams(home,nstep,matms,den,tem);

	//     register to receive mouse clicks
	addMouseListener(new MDmouse());
    }

    public void start()
    {
	runner = new Thread(this);
	stopFlag = false;
	runner.start();
    }

    public void stop()
    {
	stopFlag = true;
    }

    public void run()
    {

	while (true)
	    {
		dynamics.Verlet();
		if(dynamics.nstep%19==0)
		    repaint();
		try {
		    Thread.sleep(5);
		} catch (InterruptedException e) { };
		if (stopFlag)
		    return;
	    }
    }

    public void update(Graphics g)
    {
	paint(g);
    }

    public void update(double xden, double xtem)
    {
	den=xden;
	tem=xtem;
    }

    public void paint(Graphics g)
    {
	int i;

	offscreenImg = createImage(tile,tile);
	offscreenG = offscreenImg.getGraphics();

	box=Math.sqrt((double)natms/den);
	scrn=(double)tile/box;
	dia=(int)(scrn);
	hdi=(int)(scrn/2.0);

	//     	Draw background

	offscreenG.setColor(Color.black);
	offscreenG.fillRect(0,0,tile,tile);

	//     	Draw atoms

	offscreenG.setColor(Color.red);

	for (i=0;i<natms;i++)
	    {
		xpos[i]=(int)(scrn*dynamics.x[i])-hdi;
		ypos[i]=(int)(scrn*dynamics.y[i])-hdi;
		if(ident==i)  offscreenG.setColor(Color.yellow);
		offscreenG.fillOval(xpos[i],ypos[i],dia,dia);
		if (xpos[i]>tile-dia)
		    {
			offscreenG.fillOval(xpos[i]-tile,ypos[i],dia,dia);

			if (ypos[i]>tile-dia)
			    {
				offscreenG.fillOval(xpos[i],ypos[i]-tile,dia,dia);
				offscreenG.fillOval(xpos[i]-tile,ypos[i]-tile,dia,dia);
			    }
		    }
		else if (xpos[i]<dia)
		    {
			offscreenG.fillOval(xpos[i]+tile,ypos[i],dia,dia);

			if (ypos[i]<dia)
			    {
				offscreenG.fillOval(xpos[i],ypos[i]+tile,dia,dia);
				offscreenG.fillOval(xpos[i]+tile,ypos[i]+tile,dia,dia);
			    }
		    }
		if (ypos[i]>tile-dia)
		    {
			offscreenG.fillOval(xpos[i],ypos[i]-tile,dia,dia);

			if (xpos[i]<dia)
			    {
				offscreenG.fillOval(xpos[i]+tile,ypos[i],dia,dia);
				offscreenG.fillOval(xpos[i]+tile,ypos[i]-tile,dia,dia);
			    }
		    }
		else if (ypos[i]<dia)
		    {
			offscreenG.fillOval(xpos[i],ypos[i]+tile,dia,dia);

			if (xpos[i]>tile-dia)
			    {
				offscreenG.fillOval(xpos[i]-tile,ypos[i],dia,dia);
				offscreenG.fillOval(xpos[i]-tile,ypos[i]+tile,dia,dia);
			    }
		    }
		if(ident==i) offscreenG.setColor(Color.red);

	    }


	g.drawImage(offscreenImg,0,0,this);
    }

    public void destroy()
    {
	offscreenG.dispose();
    }

    public Dimension getMinimumSize() {
	return new Dimension(tile,tile);
    }

    public Dimension getPreferredSize() {
	return getMinimumSize();
    }


    class MDmouse extends MouseAdapter
    {
	int i;
	public void mouseClicked(MouseEvent me)
	{
	    mouseX=me.getX();
	    mouseY=me.getY();
	    for(i=0;i<natms;i++)
		{
		    if((Math.pow(((xpos[i]+hdi)-mouseX),2))+(Math.pow(((ypos[i]+hdi)-mouseY),2))<(Math.pow((dia/2),2)))
			{
			    if(ident==i)
				ident=-1;
			    else
				ident=i;
			}

		}
	}
    }

}



