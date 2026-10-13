//Copyright Daresbury Laboratory
public class Simulate
{
    Democritus home;
    GraphRDF grdf;
    GraphMSD gmsd;
    GraphVAC gvac;
    private int i,j,k,matms,ix,nav;
    protected int counter, natms, nstep;
    private int dtcnt,cntmsd,cntvac;
    private double[] vx,vy,fx,fy,x0,y0,vx0,vy0,R2,vac;
    protected double[] gor,TR2,vac2,x,y;
    private double sid,mx,my,scl,tst,rct,alp,bet,bol,sdis,svel, pi;
    protected double tot,totav,pot,vir,kin,tmp,tmpav,den,tem,dtm,vol,prs,prsav,box,dr;
    private double rr0,rrr,rr3,rc2,rsq,rrs,dx,dy,sg6,sg12,gam;
    private final int num=128;
    private final int nub=250;

    public void setParams(Democritus here, int istep, int iatms,
			  double xden, double xtem)
    {
	//     	create arrays and set up simulation

	nstep=istep;
	counter=0;
	nav=0;
	dtcnt=0;
	sdis=0.0;
	svel=0.0;
	cntmsd=0;
	cntvac=0;
	matms=iatms;
	den=xden;
	tem=xtem;
	home=here;

	setVerlet();
	setLattice();

    }

    public void setVerlet()
    {

	//     	set inital Verlet variables

	tot=0.0;
	totav=0.0;
	pot=0.0;
	kin=0.0;
	prs=0.0;
	prsav=0.0;
	tmpav=0.0;
	bol=0.004;
	dtm=0.001;
	tst=dtm*0.5;
	natms=matms*matms;
	vol=(double)natms/den;
	box=Math.sqrt(vol);
	sid=Math.sqrt(vol/natms);

	x = new double[natms];
	y = new double[natms];
	x0 = new double[natms];
	y0 = new double[natms];
	R2 = new double[nub];
	TR2 = new double[nub];
	vac = new double[nub];
	vac2 = new double[nub];
	gor = new double[num];
	vx = new double[natms];
	vy = new double[natms];
	vx0 = new double[natms];
	vy0 = new double[natms];
	fx = new double[natms];
	fy = new double[natms];

	rct=0.5*box;
	dr = rct/num;
	alp=24.0*(2.0*Math.pow(rct,-12)-Math.pow(rct,-6))/rct;
	bet=4.0*(Math.pow(rct,-12)-Math.pow(rct,-6))+alp*rct;

    }

    public void setLattice()
    {

	//     	set initial atom positions

	k=0;
	for (i=0;i<matms;i++)
	    {
		for (j=0;j<matms;j++)
		    {
			x[k]=(j+0.5)*sid;
			y[k]=(i+0.5)*sid;
			k++;
		    }
	    }

	//     	set initial velocities

	mx=0.0;
	my=0.0;

	// initialise the rdf array
	for(i=0;i<num;i++)
	    {
		gor[i]=0.0;
	    }

	//initialise the msd arrays

	for(i=0;i<natms;i++)
	    {
		x0[i]=0.0;
		y0[i]=0.0;
	    }

	for(i=0;i<nub;i++)
	    R2[i]=0.0;

	//initialise the vac arrays
	for(i=0;i<nub;i++)
	    vac[i]=0.0;

	for(i=0;i<natms;i++)
	    {
		vx0[i]=0.0;
		vy0[i]=0.0;
	    }

	for (k=0;k<natms;k++)
	    {
		vx[k]=Math.random()-0.5;
		vy[k]=Math.random()-0.5;
		mx+=vx[k];
		my+=vy[k];
	    }

	mx=mx/natms;
	my=my/natms;

	//     	remove net system momentum

	kin=0.0;

	for (k=0;k<natms;k++)
	    {
		vx[k]-=mx;
		vy[k]-=my;
		kin+=vx[k]*vx[k]+vy[k]*vy[k];
	    }

	// temperature scaling
	kin=0.5*kin;
	tmp=kin/(bol*natms);
	scl=Math.sqrt(tem/tmp);

	for (k=0;k<natms;k++)
	    {
		vx[k]=scl*vx[k];
		vy[k]=scl*vy[k];
	    }

	//     	calculate initial forces
	Forces();

    }

    public void update(double xden, double xtem)
    {
	double fac;

	nstep=0;
	nav=0;

	if (xden != den)
	    {
		fac=Math.sqrt(den/xden);
		box*=fac;
		rct=0.5*box;
		dr=rct/num;
		alp=24.0*(2.0*Math.pow(rct,-12)-Math.pow(rct,-6))/rct;
		bet=4.0*(Math.pow(rct,-12)-Math.pow(rct,-6))+alp*rct;
		vol*=(fac*fac);
		den=xden;

		for (i=0;i<natms;i++)
		    {
			x[i]*=fac;
			y[i]*=fac;
		    }
	    }
	if(xtem!=tem)
	    tem=xtem;
    }

    public void update(GraphRDF grf){   //updating the graph object (from null)
	counter=1;
	for(i=0;i<num;i++)
	    gor[i]=0.0;
	grdf=grf;                       //when the button is pressed.
    }

    public void update2(GraphMSD grf){
	dtcnt=0;
	cntmsd=0;
	gmsd=grf;
    }

    public void update3(GraphVAC gvf){
	cntvac=0;
	gvac=gvf;
    }

    public void Verlet()
    {
	int k;

	pot=0.0;
	kin=0.0;
	vir=0.0;
	nstep++;
	nav++;
	if(home.qn)
	    counter++;

	if(home.qm)
	    dtcnt++;


	//	First half step motion

	for (k=0;k<natms;k++)
	    {
		vx[k]+=tst*fx[k];
		vy[k]+=tst*fy[k];
		x[k]+=dtm*vx[k];
		y[k]+=dtm*vy[k];
	    }

	//     	new forces

	Forces();

	//     	Second half step motion

	for (k=0;k<natms;k++)
	    {
		vx[k]+=tst*fx[k];
		vy[k]+=tst*fy[k];
		kin+=vx[k]*vx[k]+vy[k]*vy[k];

		if(home.qm){
		    x0[k]+=vx[k]*dtm;
		    y0[k]+=vy[k]*dtm;
		}
	    }


	kin=0.5*kin;
	tmp=kin/(bol*natms);
	tmpav=((tmpav*(nav-1))/nav)+(tmp/nav);  //rolling average
	tot=kin+pot;
	totav=((totav*(nav-1))/nav)+(tot/nav);  //rolling average
	prs=(kin-0.5*vir)/vol;
	prsav=((prsav*(nav-1))/nav)+(prs/nav);  //rolling average

	if (home != null && nstep%25 == 1)
	    home.update();


	//     	Restore periodic boundary

	for (k=0;k<natms;k++)
	    {
		if (x[k]>box) x[k]-=box;
		if (y[k]>box) y[k]-=box;
		if (x[k]<0.0) x[k]+=box;
		if (y[k]<0.0) y[k]+=box;
	    }


	if (nstep%10==1)
	    {
		mx=0.0;
		my=0.0;
		scl=Math.sqrt(tem/tmp);

		// calculate system momentum
		for (k=0;k<natms;k++)
		    {
			mx+=vx[k];
			my+=vy[k];
		    }

		mx=mx/natms;
		my=my/natms;
		// remove net system momentum again
		for (k=0;k<natms;k++)
		    {
			vx[k]-=mx;
			vy[k]-=my;
		    }
		// temperature scaling
		for (k=0;k<natms;k++)
		    {
			vx[k]=scl*vx[k];
			vy[k]=scl*vy[k];
		    }
	    }

	if(home.qn && counter%10==1)
	    grdf.update();


	if (home.qm) {
	    if(dtcnt==1){
		for(i=0;i<nub;i++)
		    R2[i]=0.0;
		for(i=0;i<natms;i++)
		    {
			x0[i]=0.0;
			y0[i]=0.0;
		    }
	    }
	    if(dtcnt%10==0){
		sdis=0.0;
		for(i=0;i<natms;i++)
		    {
			sdis += (Math.pow(x0[i],2.0)+Math.pow(y0[i],2.0));
		    }
		R2[cntmsd]+=(sdis/(double)natms);
		cntmsd++;
	    }
	    if(dtcnt%(10*nub)==0){
		for(i=0;i<nub;i++)
		    TR2[i]=R2[i];
		gmsd.update();
		cntmsd=0;
		for(i=0;i<natms;i++)
		    {
			x0[i]=0.0;
			y0[i]=0.0;
		    }
	    }
	}

	if (home.qt) {
		if(cntvac==0){
		for(i=0;i<nub;i++)
		    vac[i]=0.0;


		for(i=0;i<natms;i++)
		    {
			vx0[i]=vx[i];
			vy0[i]=vy[i];
		    }
		}
	    svel=0.0;
	    for(i=0;i<natms;i++)
		{
		    svel+=(vx0[i]*vx[i]+vy0[i]*vy[i]);
		}
	    vac[cntvac]+=(svel/(double)natms);
	    cntvac++;

	    if(cntvac%nub==0){
		for(i=0;i<nub;i++)
		    vac2[i]=vac[i];
		cntvac=0;
		gvac.update();
	    }
	}
    }

    public void Forces()
    {


	rc2=rct*rct;

	pot=0.0;
	vir=0.0;

	for (i=0;i<natms;i++)
	    {
		fx[i]=0.0;
		fy[i]=0.0;
	    }

	for (i=0;i<natms-1;i++)
	    {
		for (j=i+1;j<natms;j++)
		    {
			dx=x[j]-x[i];
			dy=y[j]-y[i];
			if (dx>rct) dx-=box;
			if (dy>rct) dy-=box;
			if (dx<-rct) dx+=box;
			if (dy<-rct) dy+=box;
			rsq=dx*dx+dy*dy;

			if (rsq<rc2)
			    {
				rrr=Math.sqrt(rsq);
				if (home.qn) {
				    ix = (int)(rrr/dr);   //  rdf
				    gor[ix]+=1.0;         //  rdf
				}
				rrs=1.0/rsq;
				rr0=rrr*rrs;
				rr3=rr0*rrs;
				sg6=rr3*rr3;
				sg12=sg6*sg6;
				pot+=4.0*(sg12-sg6)+alp*rrr-bet;
				gam=24.0*rrs*(2.0*sg12-sg6)-alp*rr0;
				if(gam>1000000) gam=1000000;
				vir-=gam*rsq;
				fx[i]-=gam*dx;
				fy[i]-=gam*dy;
				fx[j]+=gam*dx;
				fy[j]+=gam*dy;
			    }
		    }
	    }

    }
}
