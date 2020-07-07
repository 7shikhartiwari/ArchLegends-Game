
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

abstract class sidekick implements Cloneable,Comparable<sidekick>{
	protected int armor;
	protected int death;
	protected double s_hp;
	protected String className;
	protected double s_xp;
	protected int s_attackfield; 
	protected int special_power;
	sidekick(double s_hp,int s_xp,int s_attackfield,String className){
		this.s_hp=s_hp;
		this.s_xp=s_xp;
		this.s_attackfield=s_attackfield;
		this.className=className;
		this.special_power=0;
		this.death=0;
		this.armor=5;
	}
	public sidekick clone() {
		try {
			sidekick copy=(sidekick) super.clone();
			return copy;
		} catch(CloneNotSupportedException e){
			return null;
		}
	}
	@Override
	public boolean equals(Object o1) {
		if(o1 instanceof sidekick) {
			sidekick o=(sidekick) o1;
			return (s_xp==o.s_xp);
		}
		else {
			return false;
		}
		
	}
	@Override
	public int compareTo(sidekick o) {
		if(s_xp==o.s_xp)
			return 0;
		else if(s_xp<o.s_xp)
			return -1;
		else
			return 1;
	}
	
	public abstract void s_attack(monster n);

}

class minion extends sidekick{
	
	public minion() {
		super(100,0,1,"minion");
	}
	@Override
	public void s_attack(monster m) {
		m.m_hp=m.m_hp-s_attackfield;
		System.out.println("sidekick attacked and inflicted "+s_attackfield+" damage to the monster.");
		if(s_hp<0) {
			s_hp=0;
		}
			
	}
	@Override
	public minion clone() {
		minion p=(minion) super.clone();
		return p;
	}
}

class knight extends sidekick{
	public knight() {
		super(100,0,2,"knight");
	}
	public void s_attack(monster m) {
		m.m_hp=m.m_hp-s_attackfield;
		System.out.println("sidekick attacked and inflicted "+s_attackfield+" damage to the monster.");
		if(s_hp<0) {
			s_hp=0;
		}
	}
	@Override
	public knight clone() {
		knight p=(knight) super.clone();
		return p;
	}
}

abstract class hero{
	protected int h_level;
	protected double h_damage;
	protected int h_defend;
	protected int moves_count;
	protected int special_count;
	protected String name; 
	protected double h_hp;
	protected double h_xp;
	protected int attack_field;
	protected int defence_field;
	protected int def_sidekick;
	protected ArrayList<sidekick> sidekick_list;
	public hero(String name) {
		this.name=name;
		h_hp=100;
		h_xp=0;
		h_level=1;
		moves_count=0;
		h_damage=0;
		h_defend=0;
		sidekick_list=new ArrayList<>();
		def_sidekick=0;
	}
	public abstract void attack(monster m);
	public abstract void defense(monster m);
	public abstract void superpower(monster m);
	public void sort() {
		int s=sidekick_list.size();
		for(int i=0;i<s-1;i++) {
			for(int j=i+1;j<s;j++) {
				if(!(sidekick_list.get(i).equals(sidekick_list.get(j)))) {
					if(sidekick_list.get(i).compareTo(sidekick_list.get(j))<0) {
						Collections.swap(sidekick_list, i, j);
					}
				}
			}
		}
	}
	
}

abstract class monster{
	Random ran=new Random();
	protected double m_hp;
	protected double m_damage;
	protected int m_level;
	protected int weak;
	public monster(int m_level,int m_hp) {
		this.m_level=m_level;
		this.m_hp=m_hp;
		weak=0;
		m_damage=0;
	}
	public abstract void harm(hero h);
	public abstract void harm_sidekick(sidekick sk2);
	
	
}

class warrior extends hero{
	public warrior(String name1) {
		super(name1);
		attack_field=10;
		defence_field=3;
	}
	@Override
	public void attack(monster m) {
		moves_count=moves_count+1;
		if(special_count>0) {
			m.m_hp=m.m_hp-(attack_field+5);
			h_damage=attack_field+5;
			special_count=special_count-1;
		}
		else{
			m.m_hp=m.m_hp-attack_field;	
			h_damage=attack_field;
		}
		m.weak=0;
	}
	@Override
	public void defense(monster m) {
		moves_count=moves_count+1;
		if(special_count>0) {
			m.weak=defence_field+5;
			h_defend=defence_field+5;
			special_count=special_count-1;
		}
		else if(def_sidekick!=0) {
			m.weak=defence_field+5;
			h_defend=defence_field+5;
			if(m.weak!=9) {
				m.weak=9;
				h_defend=9;
			}
		}
		else {
			m.weak=defence_field;
			h_defend=defence_field;
		}
		
	}
	@Override
	public void superpower(monster m) {
		if(moves_count>=3) {
			special_count=3;
			moves_count=0;
			System.out.println("Special power activated\r\n"+"Performing special attack\r\n"+"Attack and Defence attributes boosted by 5 for next three moves");
			
		}
	}
}

class mage extends hero{
	public mage(String name1) {
		super(name1);
		attack_field=5;
		defence_field=5;
	}
	@Override
	public void attack(monster m) {
		h_damage=attack_field;
		moves_count=moves_count+1;
		m.m_hp=m.m_hp-attack_field;
		m.weak=0;
		if(special_count>0) {
			m.m_hp=(int)(m.m_hp*0.95);
			special_count=special_count-1;
		}
	}
	@Override
	public void defense(monster m) {
		moves_count=moves_count+1;
		m.weak=defence_field;
		h_defend=defence_field;
		if(special_count>0) {
			m.m_hp=(int)(m.m_hp*0.95);
			special_count=special_count-1;
		}
		else if(def_sidekick!=0) {
			m.weak=defence_field+5;
			h_defend=defence_field+5;
			if(m.weak!=9) {
				m.weak=9;
				h_defend=9;
			}
		}
	}
	@Override
	public void superpower(monster m) {
		if(moves_count>=3) {
			special_count=3;
			moves_count=0;
			System.out.println("Special power activated\r\n"+"Performing special attack\r\n"+"Reducing monsters hp by 5% for next three moves");
			
		}
	}
}

class thief extends hero{
	public thief(String name1) {
		super(name1);
		attack_field=6;
		defence_field=4;
	}
	@Override
	public void attack(monster m) {
		h_damage=attack_field;
		moves_count=moves_count+1;
		m.m_hp=m.m_hp-attack_field;
		m.weak=0;
	}
	@Override
	public void defense(monster m) {
		moves_count=moves_count+1;
		m.weak=defence_field;
		h_defend=defence_field;
		if(def_sidekick!=0) {
			m.weak=defence_field+5;
			h_defend=defence_field+5;
			if(m.weak!=9) {
				m.weak=9;
				h_defend=9;
			}
		}
	}
	@Override
	public void superpower(monster m) {
		moves_count=moves_count+1;
		int u=(int)(0.3*m.m_hp);
		h_hp=h_hp+(int)(0.3*m.m_hp);
		m.m_hp=m.m_hp-(int)(0.3*m.m_hp);
		m.weak=0;
		System.out.println("Special power activated\r\n"+"Performing special attack\r\n"+"You have stolen "+u+" Hp from the monster!\n"+"Special power deactivated");
		
	}
}

class healer extends hero{
	public healer(String name1) {
		super(name1);
		attack_field=4;
		defence_field=8;
	}
	@Override
	public void attack(monster m) {
		h_damage=attack_field;
		moves_count=moves_count+1;
		m.m_hp=m.m_hp-attack_field;
		m.weak=0;
		if(special_count>0) {
			h_hp=(int)(1.05*h_hp);
			special_count=special_count-1;
		}
	}
	@Override
	public void defense(monster m) {
		moves_count=moves_count+1;
		m.weak=defence_field;
		h_defend=defence_field;
		if(special_count>0) {
			h_hp=(int)(1.05*h_hp);
			special_count=special_count-1;
		}
		if(def_sidekick!=0) {
			m.weak=defence_field+5;
			h_defend=defence_field+5;
			if(m.weak!=9) {
				m.weak=9;
				h_defend=9;
			}
		}
	}
	@Override
	public void superpower(monster m) {
		if(moves_count>=3) {
			special_count=3;
			moves_count=0;
			System.out.println("Special power activated\r\n"+"increasing own hp by 5% for next three moves.");
			
		}
	}
}

class goblin extends monster{
	public goblin() {
		super(1,100);
	}
	public void harm(hero h) {
		double sum=0;
		for(int i=0;i<m_hp/4;i++) {
			sum+=(i-m_hp/8)*(i-m_hp/8);
		}
		int damage=(int)(ran.nextGaussian()*(Math.sqrt(sum/(m_hp/4)))+(double)m_hp/8);
		if(damage-weak<0) {
			damage=0;
			m_damage=0;
		}
		else {
			h.h_hp=h.h_hp-damage+weak;
			m_damage=damage-weak;
		}
	}
	public void harm_sidekick(sidekick s) {
		s.s_hp=s.s_hp-(m_damage)*(1.5);
		if(s.s_hp<=0) {
			s.s_hp=0;
		}
		System.out.println("sidekick's hp: "+s.s_hp);
	}
}

class zombie extends monster{
	public zombie() {
		super(2,150);
	}
	public void harm(hero h) {
		double sum=0;
		for(int i=0;i<m_hp/4;i++) {
			sum+=(i-m_hp/8)*(i-m_hp/8);
		}
		int damage=(int)(ran.nextGaussian()*(Math.sqrt(sum/(m_hp/4)))+(double)m_hp/8);
		if(damage-weak<0) {
			damage=0;
			m_damage=0;
		}
		else {
			h.h_hp=h.h_hp-damage+weak;
			m_damage=damage-weak;
		}
	}
	public void harm_sidekick(sidekick s) {
		s.s_hp=s.s_hp-(m_damage)*(1.5);
		if(s.s_hp<=0) {
			s.s_hp=0;
		}
		System.out.println("sidekick's hp: "+s.s_hp);
	}
}

class fiends extends monster{
	public fiends() {
		super(3,200);
	}
	public void harm(hero h) {
		double sum=0;
		for(int i=0;i<m_hp/4;i++) {
			sum+=(i-m_hp/8)*(i-m_hp/8);
		}
		int damage=(int)(ran.nextGaussian()*(Math.sqrt(sum/(m_hp/4)))+(double)m_hp/8);
		if(damage-weak<0) {
			damage=0;
			m_damage=0;
		}
		else {
			h.h_hp=h.h_hp-damage+weak;
			m_damage=damage-weak;
		}
		
	}
	public void harm_sidekick(sidekick s) {
		s.s_hp=s.s_hp-(m_damage)*(1.5);
		if(s.s_hp<=0) {
			s.s_hp=0;
		}
		System.out.println("sidekick's hp: "+s.s_hp);
	}
}
class lionfang extends monster{
	public lionfang() {
		super(4,250);
	}
	public void harm(hero h) {
		int t=ran.nextInt(10);
		if(t==1) {
			h.h_hp=h.h_hp/2;
			m_damage=h.h_hp;
		}
		else {
			double sum=0;
			for(int i=0;i<m_hp/4;i++) {
				sum+=(i-m_hp/8)*(i-m_hp/8);
			}
			int damage=(int)(ran.nextGaussian()*(Math.sqrt(sum/(m_hp/4)))+(double)m_hp/8);
			if(damage-weak<0) {
				damage=0;
				m_damage=0;
			}
			else {
				h.h_hp=h.h_hp-damage+weak;
				m_damage=damage-weak;
			}
		}
	}
	public void harm_sidekick(sidekick s) {
		s.s_hp=s.s_hp-(m_damage)*(1.5);
		if(s.s_hp<=0) {
			s.s_hp=0;
		}
		System.out.println("sidekick's hp: "+s.s_hp);
	}
}
class node{
	private node back_n;
	private int number;
	private monster m;
	private ArrayList<node> forward_n=new ArrayList<>();
	public node(int number){
		back_n=null;
		this.number=number;
		m=null;
	}
	public node getBack_n() {
		return back_n;
	}
	public void setBack_n(node back_n) {
		this.back_n = back_n;
	}
	
	public monster getM() {
		return m;
	}
	public void setM(monster m) {
		this.m = m;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public ArrayList<node> getForward_n() {
		return forward_n;
	}

	
}
public class game2 {
	static ArrayList<String> ex_name=new ArrayList<>();
	static ArrayList<String> ex_category=new ArrayList<>();
	static ArrayList<node> graph=new ArrayList<>();
	static int cs=0;
	public static void addEdge(int a,int b) {
		graph.get(a).getForward_n().add(graph.get(b));
	}
	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		boolean a=true;
		while(a!=false) {
			System.out.println("Welcome to ArchLegends\n" + "Choose your option\n" + "1) New User\n" + "2) Existing User\n" + "3) Exit");
			int n=s.nextInt();
			if((n==1)||(n==2)) {
				System.out.println("Enter username :");
				String ch=s.next();
				if(n==1) {
					System.out.println("Choose a Hero\n" +"1) Warrior\n"+"2) Thief\n"+"3) Mage\n"+"4) Healer");
					int n1=s.nextInt();
					if(n1==1) {
						warrior ob=new warrior(ch);
						ex_name.add(ch);
						ex_category.add("warrior");
						System.out.println("User Creation done. "+"Username: "+ch+". Hero type: Warrior. Log in to play the game . Exiting");
					}
					else if(n1==2) {
						thief ob=new thief(ch);
						ex_name.add(ch);
						ex_category.add("thief");
						System.out.println("User Creation done. "+"Username: "+ch+". Hero type: Thief. Log in to play the game . Exiting");
					}
					else if(n1==3) {
						mage ob=new mage(ch);
						ex_name.add(ch);
						ex_category.add("mage");
						System.out.println("User Creation done. "+"Username: "+ch+". Hero type: Mage. Log in to play the game . Exiting");
					}
					else {
						healer ob=new healer(ch);
						ex_name.add(ch);
						ex_category.add("healer");
						System.out.println("User Creation done. "+"Username: "+ch+". Hero type: Healer. Log in to play the game . Exiting");
					}
					
				}
				if(n==2){
					int k=-1;
					for(int i=0;i<ex_name.size();i++) {
						if(ex_name.get(i).equals(ch)) {
							k=i;
							System.out.println("User found! logging in...");
							break;
						}
					}
					if(k==-1) {
						System.out.println("User not found! try again");
						break;
					}
					
					hero ob=null;
					if(ex_category.get(k).equals("warrior")) {
						ob=new warrior(ch);
					}
					else if(ex_category.get(k).equals("thief")) {
						ob=new thief(ch);
					}
					else if(ex_category.get(k).equals("mage")) {
						ob=new mage(ch);
					}
					else if(ex_category.get(k).equals("healer")) {
						ob=new healer(ch);
					}
					// gameplay starts
					System.out.println("Welcome "+ob.name);
					for(int i=0;i<=10;i++) {
						node e=new node(i);
						graph.add(e);
					}
					addEdge(0,1);
					addEdge(0,4);
					addEdge(0,7);
					addEdge(1,2);
					addEdge(1,5);
					addEdge(1,8);
					addEdge(2,3);
					addEdge(2,6);
					addEdge(2,9);
					addEdge(3,10);
					addEdge(4,2);
					addEdge(4,5);
					addEdge(4,8);
					addEdge(5,3);
					addEdge(5,6);
					addEdge(5,9);
					addEdge(6,10);
					addEdge(7,2);
					addEdge(7,5);
					addEdge(7,8);
					addEdge(8,3);
					addEdge(8,6);
					addEdge(8,9);
					addEdge(9,10);
					
					for(int i=1;i<graph.size()-1;i++) {
						Random rand=new Random();
						int r=1+rand.nextInt(3);
						monster ob1=null;
						if(r==1)
							ob1=new goblin();
						else if(r==2)
							ob1=new zombie();
						else
							ob1=new fiends();
						graph.get(i).setM(ob1);
					}
					monster ob2=new lionfang();
					graph.get(10).setM(ob2);
					///////////////////////////////
					
					System.out.println("Hint...........");

					int a2=0;
					int x1=graph.get(1).getM().m_level;
					int x2=graph.get(2).getM().m_level;
					int x3=graph.get(3).getM().m_level;
					int x4=graph.get(4).getM().m_level;
					int x5=graph.get(5).getM().m_level;
					int x6=graph.get(6).getM().m_level;
					int x7=graph.get(7).getM().m_level;
					int x8=graph.get(8).getM().m_level;
					int x9=graph.get(9).getM().m_level;
					
					if((x1<=x4)&&(x1<=x7)) {
						a2=graph.get(1).getNumber();
					}
					if(((x4<=x1)&&(x4<=x7))) {
						a2=graph.get(4).getNumber();
					}
					if((x7<=x4)&&(x7<=x1)) {
						a2=graph.get(7).getNumber();
					}
					/////
					int a3=0;
					if((x2<=x5)&&(x2<=x8)) {
						a3=graph.get(2).getNumber();
					}
					if((x5<=x2)&&(x5<=x8)){
						a3=graph.get(5).getNumber();
					}
					if((x8<=x5)&&(x8<=x2)){
						a3=graph.get(8).getNumber();
					}
					////
					int a4=0;
					if((x3<=x6)&&(x3<=x9)){
						a4=graph.get(3).getNumber();
					}
					if((x6<=x3)&&(x6<=x9)){
						a4=graph.get(6).getNumber();
					}
					if((x9<=x6)&&(x9<=x3)){
						a4=graph.get(9).getNumber();
					}
					System.out.println("0"+" -> "+a2+" -> "+a3+" -> "+a4+" -> "+"10");
					
					//////////////////////////////
					node cur=graph.get(0);
					boolean b=true;
					//traversing the graph
					int c1=0;
					while(b!=false) {
						if(cur.equals(graph.get(0))) {
							System.out.println("You are at initial location "+". Choose path");
							System.out.println("1) Go to location "+cur.getForward_n().get(0).getNumber());
							System.out.println("2) Go to location "+cur.getForward_n().get(1).getNumber());
							System.out.println("3) Go to location "+cur.getForward_n().get(2).getNumber());
							System.out.println("Enter -1 to exit");
							int k3=s.nextInt();
							if(((k3==1)||(k3==2)||(k3==3))&&(cs!=0)) {
								cur.getForward_n().get(k3-1).setBack_n(cur);
								cur=cur.getForward_n().get(k3-1);
								System.out.println("Moving to "+cur.getNumber());
								//cloning of monster
								monster clone=null;
								if(cur.getM().m_level==1) {
									clone=new goblin();
								}
								else if(cur.getM().m_level==2) {
									clone=new zombie();
								}
								else if(cur.getM().m_level==3) {
									clone=new fiends();
								}
								System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
								//fighting a monster
								int c=0;
								ob.sort();
								System.out.println("Type yes if you want to use a side kick. Else type no.");
								String g4=s.next();
								sidekick sk2=null;
								
								//do part of comparison here change this null
								sidekick sk21=null;
								sidekick sk22=null;
								sidekick sk23=null;
								if(g4.equals("yes")&&(ob.sidekick_list.size()!=0)) {
									sk2=ob.sidekick_list.get(0);
									System.out.println("You have a sidekick "+sk2.className+" with you.Attack of sidekick is"+sk2.s_attackfield);
									if((sk2.special_power==0)&&(sk2.className.equals("minion"))) {
										System.out.println("Press c to use cloning ability. Else press f to move to the fight");
										String g5=s.next();
										if(g5.equals("c")) {
											c=c+1;
											sk21=sk2.clone();
											sk22=sk2.clone();
											sk23=sk2.clone();
										}
									}
									
								}
								else if((g4.equals("yes"))&&(ob.sidekick_list.size()==0)) {
									System.out.println("Sorry! you have no sidekicks left");
									sk2=null;
								}
								else if(g4.equals("no")) {
									System.out.println("sidekick not chosen");
									sk2=null;
								}
								
								int q=(int)ob.h_hp;
								int q2=(int)clone.m_hp;
								boolean b1=true;
								//fight begins			
								while(b1!=false) {
									if(ob.h_hp<=0) {
										cur=graph.get(0);
										System.out.println("You lost try again from initial position..");
										ob.h_hp=100;
										ob.h_xp=0;
										ob.h_level=1;
										cs=0;
										if(sk2!=null) {
											sk2.armor=0;
											sk2.s_hp=100;
										}
										break;
									}
									System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
									if(ob.moves_count>=3)
										System.out.println("3)Special Attack");
									int t1=s.nextInt();
									if(t1==1) {
										ob.attack(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										//sidekick attack
										if(sk2!=null) {
											if(sk21!=null) {
												if((sk2.special_power==0)&&(sk2.className.equals("minion"))&&(sk2.death==0)){
													c=c+1;
													sk2.s_attack(clone);
													sk21.s_attack(clone);
													sk22.s_attack(clone);
													sk23.s_attack(clone);
													
												}
											}
											else {
												sk2.s_attack(clone);
											}	
										}
									}
									if(t1==2) {
										if((sk2!=null)&&(clone.m_level==2)&&(sk2.className.compareTo("knight")==0)) {
											System.out.println("Defence mechanism of knight is in use.");
											ob.def_sidekick=5;
										}
										ob.defense(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										
										System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									if(t1==3) {
										ob.superpower(clone);
										ob.moves_count=0;
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									clone.harm(ob);      //monster attacks here
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
									System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									//////////////////
									if(sk2!=null) {
										if(sk21!=null) {
											if((sk2!=null)&&(sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
												if((sk21.death==0)&&(sk22.death==0)&&(sk23.death==0)) {
													clone.harm_sidekick(sk21);
													clone.harm_sidekick(sk22);
													clone.harm_sidekick(sk23);
												}
											}
											else {
												sk2.death=1;
												sk2.s_hp=0;
												if(sk21!=null) {
													sk21.death=1;
													sk22.death=1;
													sk23.death=1;
													sk21.s_hp=0;
													sk22.s_hp=0;
													sk23.s_hp=0;
												}
											}
										}
										else {
											if((sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
											}
										}
										if(sk2.s_hp<=0) {
											sk2.death=1;
											System.out.println("sidekick died");
											if(sk21!=null) {
												sk21.death=1;
												System.out.println("sidekick died");
												sk22.death=1;
												System.out.println("sidekick died");
												sk23.death=1;
												System.out.println("sidekick died");
											}
										}
									}
									
									/////////////////
									if(clone.m_hp<=0) {
										int xp=clone.m_level*20;
										
										System.out.println("Monster killed\n"+xp+" xp awarded");
										if(sk2!=null) {
											System.out.println("sidekick gained "+xp/10+" xp");
											sk2.s_xp=sk2.s_xp+xp/10; 
											if(sk2.s_xp%5==0) {
												sk2.s_attackfield+=1;
											}
											sk2.s_hp=100;
										}
										System.out.println("Fight won ! proceeding to next location");
										System.out.println("If you would like to buy a sidekick, type yes. Else no to upgrade level.");
										String g=s.next();
										if(g.equals("yes")) {
											System.out.println("Your current xp is : "+clone.m_level*20);
											System.out.println("If you want to buy a minion, press 1\n"+"If you want to buy a knight, press 2");
											int g2=s.nextInt();
											sidekick sk=null;
											if(g2==1) 
												sk=new minion();
											else if(g2==2) 
												sk=new knight();
											System.out.print("xp to spend : ");
											int g3=s.nextInt();
											System.out.println("You bought a sidekick "+sk.className);
											if(g2==1)
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-5));
											else
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-8));
											System.out.println("xp of sidekick is "+sk.s_xp+"\n"+"Attack of sidekick is "+sk.s_attackfield);
											ob.sidekick_list.add(sk);
											xp=xp-g3;
											
											
										}
										ob.h_xp+=xp;
										ob.h_hp=100;
										if((ob.h_xp>=20)&&(ob.h_xp<60))
											ob.h_level=2;
										if((ob.h_xp>=60)&&(ob.h_xp<120))
											ob.h_level=3;
										if((ob.h_xp>=120))
											ob.h_level=4;
										System.out.println("Level = level "+ob.h_level);
										if(ob.h_level==1) {
											ob.h_hp=100;
										}
										if(ob.h_level==2) {
											ob.h_hp=150;
										}
										if(ob.h_level==3) {
											ob.h_hp=200;
										}
										if(ob.h_level==4) {
											ob.h_hp=250;
										}
										if((c!=0)&&(sk2.special_power==0)) {
											sk2.special_power=1;
										}
										
										ob.def_sidekick=0;
										break;
									}
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									
								}	
								if(sk2!=null) {
									if(ob.sidekick_list.get(0).death==1) {
										ob.sidekick_list.remove(sk2);
									}
								}
							}
							if((k3==1)||(k3==2)||(k3==3)&&(cs==0)) {
								cur.getForward_n().get(k3-1).setBack_n(cur);
								cur=cur.getForward_n().get(k3-1);
								System.out.println("Moving to "+cur.getNumber());
								//cloning of monster
								cs=cs+1;
								monster clone=null;
								if(cur.getM().m_level==1) {
									clone=new goblin();
								}
								else if(cur.getM().m_level==2) {
									clone=new zombie();
								}
								else if(cur.getM().m_level==3) {
									clone=new fiends();
								}
								System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
								//fighting a monster
								boolean b1=true;
								int q=(int)ob.h_hp;
								int q2=(int)clone.m_hp;
								while(b1!=false) {
									if(ob.h_hp<=0) {
										cur=graph.get(0);
										System.out.println("You lost try again from initial position..");
										ob.h_hp=100;
										ob.h_xp=0;
										ob.h_level=1;
										cs=0;
										break;
									}
									System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
									if(ob.moves_count>=3)
										System.out.println("3)Special Attack");
									int t1=s.nextInt();
									if(t1==1) {
										ob.attack(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									}
									if(t1==2) {
										ob.defense(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									if(t1==3) {
										ob.superpower(clone);
										ob.moves_count=0;
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									clone.harm(ob);
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
									System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									if(clone.m_hp<=0) {
										int xp=clone.m_level*20;
										
										System.out.println("Monster killed\n"+xp+" xp awarded");
										System.out.println("FIght won ! proceed to next location");
										System.out.println("If you would like to buy a sidekick, type yes. Else no to upgrade level.");
										String g=s.next();
										if(g.equals("yes")) {
											System.out.println("Your current xp is : "+clone.m_level*20);
											System.out.println("If you want to buy a minion, press 1\n"+"If you want to buy a knight, press 2");
											int g2=s.nextInt();
											sidekick sk=null;
											if(g2==1) 
												sk=new minion();
											else if(g2==2) 
												sk=new knight();
											System.out.println("xp to spend : ");
											int g3=s.nextInt();
											System.out.println("You bought a sidekick "+sk.className);
											if(g2==1)
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-5));
											else
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-8));
											System.out.println("xp of sidekick is "+sk.s_xp+"\n"+"Attack of sidekick is "+sk.s_attackfield);
											ob.sidekick_list.add(sk);
											xp=xp-g3;
											
											
										}
										ob.h_xp+=xp;
										ob.h_hp=100;
										if((ob.h_xp>=20)&&(ob.h_xp<60))
											ob.h_level=2;
										if((ob.h_xp>=60)&&(ob.h_xp<120))
											ob.h_level=3;
										if((ob.h_xp>=120))
											ob.h_level=4;
										System.out.println("Level = level "+ob.h_level);
										if(ob.h_level==1) {
											ob.h_hp=100;
										}
										if(ob.h_level==2) {
											ob.h_hp=150;
										}
										if(ob.h_level==3) {
											ob.h_hp=200;
										}
										if(ob.h_level==4) {
											ob.h_hp=250;
										}
										break;
										
									}
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
								}
								
								
							} 
							if(k3==-1) {
								System.out.println("Exit");
								break;
							}
						}
						//##########################################################  lionfang    ##################################################################3
						else if(cur.getForward_n().get(0).equals(graph.get(10))) {
							System.out.println("You are at location "+cur.getNumber()+". Choose path");
							System.out.println("1) Go to location "+cur.getForward_n().get(0).getNumber()+" to fight LionFang monster");
							System.out.println("2)Go back");
							System.out.println("Enter -1 to go back");
							int k3=s.nextInt();
							if(k3==1) {
								cur.getForward_n().get(0).setBack_n(cur);
								cur=cur.getForward_n().get(0);
								System.out.println("Moving to "+cur.getNumber());
								//cloning of monster
								monster clone=null;
								if(cur.getM().m_level==1) {
									clone=new goblin();
								}
								else if(cur.getM().m_level==2) {
									clone=new zombie();
								}
								else if(cur.getM().m_level==3) {
									clone=new fiends();
								}
								else {
									clone=new lionfang();
								}
								System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
								//fighting a monster
								int c=0;
								ob.sort();
								System.out.println("Type yes if you want to use a side kick. Else type no.");
								String g4=s.next();
								sidekick sk2=null;
								
								//do part of comparison here change this null
								sidekick sk21=null;
								sidekick sk22=null;
								sidekick sk23=null;
								if(g4.equals("yes")&&(ob.sidekick_list.size()!=0)) {
									sk2=ob.sidekick_list.get(0);
									System.out.println("You have a sidekick "+sk2.className+" with you.Attack of sidekick is "+sk2.s_attackfield);
									if((sk2.special_power==0)&&(sk2.className.equals("minion"))) {
										System.out.println("Press c to use cloning ability. Else press f to move to the fight");
										String g5=s.next();
										if(g5.equals("c")) {
											c=c+1;
											sk21=sk2.clone();
											sk22=sk2.clone();
											sk23=sk2.clone();
										}
									}
									
								}
								else if((g4.equals("yes"))&&(ob.sidekick_list.size()==0)) {
									System.out.println("Sorry! you have no sidekicks left");
									sk2=null;
								}
								else if(g4.equals("no")) {
									System.out.println("sidekick not chosen");
									sk2=null;
								}
								
								int q=(int)ob.h_hp;
								int q2=(int)clone.m_hp;
								boolean b1=true;
								//fight begins			
								while(b1!=false) {
									if(ob.h_hp<=0) {
										cur=graph.get(0);
										System.out.println("You lost try again from initial position..");
										ob.h_hp=100;
										ob.h_xp=0;
										ob.h_level=1;
										cs=0;
										if(sk2!=null) {
											sk2.armor=0;
											sk2.s_hp=100;
										}
										break;
									}
									System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
									if(ob.moves_count>=3)
										System.out.println("3)Special Attack");
									int t1=s.nextInt();
									if(t1==1) {
										ob.attack(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										//sidekick attack
										if(sk2!=null) {
											if(sk21!=null) {
												if((sk2.special_power==0)&&(sk2.className.equals("minion"))&&(sk2.death==0)){
													c=c+1;
													sk2.s_attack(clone);
													sk21.s_attack(clone);
													sk22.s_attack(clone);
													sk23.s_attack(clone);
													
												}
											}
											else {
												sk2.s_attack(clone);
											}	
										}
									}
									if(t1==2) {
										if((sk2!=null)&&(clone.m_level==2)&&(sk2.className.compareTo("knight")==0)) {
											System.out.println("Defence mechanism of knight is in use.");
											ob.def_sidekick=5;
										}
										ob.defense(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										
										System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									if(t1==3) {
										ob.superpower(clone);
										ob.moves_count=0;
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									clone.harm(ob);      //monster attacks here
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
									System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									//////////////////
									if(sk2!=null) {
										if(sk21!=null) {
											if((sk2!=null)&&(sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
												if((sk21.death==0)&&(sk22.death==0)&&(sk23.death==0)) {
													clone.harm_sidekick(sk21);
													clone.harm_sidekick(sk22);
													clone.harm_sidekick(sk23);
												}
											}
											else {
												sk2.death=1;
												sk2.s_hp=0;
												if(sk21!=null) {
													sk21.death=1;
													sk22.death=1;
													sk23.death=1;
													sk21.s_hp=0;
													sk22.s_hp=0;
													sk23.s_hp=0;
												}
											}
										}
										else {
											if((sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
											}
										}
										if(sk2.s_hp<=0) {
											sk2.death=1;
											System.out.println("sidekick died");
											if(sk21!=null) {
												sk21.death=1;
												System.out.println("sidekick died");
												sk22.death=1;
												System.out.println("sidekick died");
												sk23.death=1;
												System.out.println("sidekick died");
											}
										}
									}
									
									/////////////////
									if(clone.m_hp<=0) {
										int xp=clone.m_level*20;
										
										System.out.println("LionFang killed.....");
										if(sk2!=null) {
											sk2.s_xp=sk2.s_xp+xp/10; 
											if(sk2.s_xp%5==0) {
												sk2.s_attackfield+=1;
											}
											sk2.s_hp=100;
										}
										
										
										ob.h_xp+=xp;
										ob.h_hp=100;
										if((ob.h_xp>=20)&&(ob.h_xp<60))
											ob.h_level=2;
										if((ob.h_xp>=60)&&(ob.h_xp<120))
											ob.h_level=3;
										if((ob.h_xp>=120))
											ob.h_level=4;
										if(ob.h_level==1) {
											ob.h_hp=100;
										}
										if(ob.h_level==2) {
											ob.h_hp=150;
										}
										if(ob.h_level==3) {
											ob.h_hp=200;
										}
										if(ob.h_level==4) {
											ob.h_hp=250;
										}
										if((c!=0)&&(sk2.special_power==0)) {
											sk2.special_power=1;
										}
										
										ob.def_sidekick=0;
										System.out.println("Congratulations...You won");
										break;
									}
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									
								}	
								if(sk2!=null) {
									if(ob.sidekick_list.get(0).death==1) {
										ob.sidekick_list.remove(sk2);
									}
								}
								break;
							}
							if(k3==2) {
								cur=cur.getBack_n();
								/////////////////////////////////////////////
								System.out.println("Moving to "+cur.getNumber());
								//cloning of monster
								monster clone=null;
								if(cur.getM().m_level==1) {
									clone=new goblin();
								}
								else if(cur.getM().m_level==2) {
									clone=new zombie();
								}
								else if(cur.getM().m_level==3) {
									clone=new fiends();
								}
								System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
								//fighting a monster
								int c=0;
								ob.sort();
								System.out.println("Type yes if you want to use a side kick. Else type no.");
								String g4=s.next();
								sidekick sk2=null;
								
								//do part of comparison here change this null
								sidekick sk21=null;
								sidekick sk22=null;
								sidekick sk23=null;
								if(g4.equals("yes")&&(ob.sidekick_list.size()!=0)) {
									sk2=ob.sidekick_list.get(0);
									System.out.println("You have a sidekick "+sk2.className+" with you.Attack of sidekick is"+sk2.s_attackfield);
									if((sk2.special_power==0)&&(sk2.className.equals("minion"))) {
										System.out.println("Press c to use cloning ability. Else press f to move to the fight");
										String g5=s.next();
										if(g5.equals("c")) {
											c=c+1;
											sk21=sk2.clone();
											sk22=sk2.clone();
											sk23=sk2.clone();
										}
									}
									
								}
								else if((g4.equals("yes"))&&(ob.sidekick_list.size()==0)) {
									System.out.println("Sorry! you have no sidekicks left");
									sk2=null;
								}
								else if(g4.equals("no")) {
									System.out.println("sidekick not chosen");
									sk2=null;
								}
								
								int q=(int)ob.h_hp;
								int q2=(int)clone.m_hp;
								boolean b1=true;
								//fight begins			
								while(b1!=false) {
									if(ob.h_hp<=0) {
										cur=graph.get(0);
										System.out.println("You lost try again from initial position..");
										ob.h_hp=100;
										ob.h_xp=0;
										ob.h_level=1;
										cs=0;
										if(sk2!=null) {
											sk2.armor=0;
											sk2.s_hp=100;
										}
										break;
									}
									System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
									if(ob.moves_count>=3)
										System.out.println("3)Special Attack");
									int t1=s.nextInt();
									if(t1==1) {
										ob.attack(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										//sidekick attack
										if(sk2!=null) {
											if(sk21!=null) {
												if((sk2.special_power==0)&&(sk2.className.equals("minion"))&&(sk2.death==0)){
													c=c+1;
													sk2.s_attack(clone);
													sk21.s_attack(clone);
													sk22.s_attack(clone);
													sk23.s_attack(clone);
													
												}
											}
											else {
												sk2.s_attack(clone);
											}	
										}
									}
									if(t1==2) {
										if((sk2!=null)&&(clone.m_level==2)&&(sk2.className.compareTo("knight")==0)) {
											System.out.println("Defence mechanism of knight is in use.");
											ob.def_sidekick=5;
										}
										ob.defense(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										
										System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									if(t1==3) {
										ob.superpower(clone);
										ob.moves_count=0;
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									clone.harm(ob);      //monster attacks here
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
									System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									//////////////////
									if(sk2!=null) {
										if(sk21!=null) {
											if((sk2!=null)&&(sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
												if((sk21.death==0)&&(sk22.death==0)&&(sk23.death==0)) {
													clone.harm_sidekick(sk21);
													clone.harm_sidekick(sk22);
													clone.harm_sidekick(sk23);
												}
											}
											else {
												sk2.death=1;
												sk2.s_hp=0;
												if(sk21!=null) {
													sk21.death=1;
													sk22.death=1;
													sk23.death=1;
													sk21.s_hp=0;
													sk22.s_hp=0;
													sk23.s_hp=0;
												}
											}
										}
										else {
											if((sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
											}
										}
										if(sk2.s_hp<=0) {
											sk2.death=1;
											System.out.println("sidekick died");
											if(sk21!=null) {
												sk21.death=1;
												System.out.println("sidekick died");
												sk22.death=1;
												System.out.println("sidekick died");
												sk23.death=1;
												System.out.println("sidekick died");
											}
										}
									}
									
									/////////////////
									if(clone.m_hp<=0) {
										int xp=clone.m_level*20;
										
										System.out.println("Monster killed\n"+xp+" xp awarded");
										if(sk2!=null) {
											System.out.println("sidekick gained "+xp/10+" xp");
											sk2.s_xp=sk2.s_xp+xp/10; 
											if(sk2.s_xp%5==0) {
												sk2.s_attackfield+=1;
											}
											sk2.s_hp=100;
										}
										System.out.println("Fight won ! proceeding to next location");
										System.out.println("If you would like to buy a sidekick, type yes. Else no to upgrade level.");
										String g=s.next();
										if(g.equals("yes")) {
											System.out.println("Your current xp is : "+clone.m_level*20);
											System.out.println("If you want to buy a minion, press 1\n"+"If you want to buy a knight, press 2");
											int g2=s.nextInt();
											sidekick sk=null;
											if(g2==1) 
												sk=new minion();
											else if(g2==2) 
												sk=new knight();
											System.out.print("xp to spend : ");
											int g3=s.nextInt();
											System.out.println("You bought a sidekick "+sk.className);
											if(g2==1)
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-5));
											else
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-8));
											System.out.println("xp of sidekick is "+sk.s_xp+"\n"+"Attack of sidekick is "+sk.s_attackfield);
											ob.sidekick_list.add(sk);
											xp=xp-g3;
											
											
										}
										ob.h_xp+=xp;
										ob.h_hp=100;
										if((ob.h_xp>=20)&&(ob.h_xp<60))
											ob.h_level=2;
										if((ob.h_xp>=60)&&(ob.h_xp<120))
											ob.h_level=3;
										if((ob.h_xp>=120))
											ob.h_level=4;
										System.out.println("Level = level "+ob.h_level);
										if(ob.h_level==1) {
											ob.h_hp=100;
										}
										if(ob.h_level==2) {
											ob.h_hp=150;
										}
										if(ob.h_level==3) {
											ob.h_hp=200;
										}
										if(ob.h_level==4) {
											ob.h_hp=250;
										}
										if((c!=0)&&(sk2.special_power==0)) {
											sk2.special_power=1;
										}
										
										ob.def_sidekick=0;
										break;
									}
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									
								}	
								if(sk2!=null) {
									if(ob.sidekick_list.get(0).death==1) {
										ob.sidekick_list.remove(sk2);
									}
								}
							}
							if(k3==-1) {
								cs=0;
								ob.h_level=1;
								ob.h_hp=100;
								ob.h_xp=0;
								break;
							}
						}
						//##################################################  else ###########################################################################
						else {
							System.out.println("You are at location "+cur.getNumber()+". Choose path");
							System.out.println("1) Go to location "+cur.getForward_n().get(0).getNumber());
							System.out.println("2) Go to location "+cur.getForward_n().get(1).getNumber());
							System.out.println("3) Go to location "+cur.getForward_n().get(2).getNumber());
							System.out.println("4) Go back");
							System.out.println("Enter -1 to go back");
							int k3=s.nextInt();
							if((k3==1)||(k3==2)||(k3==3)) {
								cur.getForward_n().get(k3-1).setBack_n(cur);
								cur=cur.getForward_n().get(k3-1);
								System.out.println("Moving to "+cur.getNumber());
								//cloning of monster
								monster clone=null;
								if(cur.getM().m_level==1) {
									clone=new goblin();
								}
								else if(cur.getM().m_level==2) {
									clone=new zombie();
								}
								else if(cur.getM().m_level==3) {
									clone=new fiends();
								}
								System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
								//fighting a monster
								int c=0;
								ob.sort();
								System.out.println("Type yes if you want to use a side kick. Else type no.");
								String g4=s.next();
								sidekick sk2=null;
								
								//do part of comparison here change this null
								sidekick sk21=null;
								sidekick sk22=null;
								sidekick sk23=null;
								if(g4.equals("yes")&&(ob.sidekick_list.size()!=0)) {
									sk2=ob.sidekick_list.get(0);
									System.out.println("You have a sidekick "+sk2.className+" with you.Attack of sidekick is"+sk2.s_attackfield);
									if((sk2.special_power==0)&&(sk2.className.equals("minion"))) {
										System.out.println("Press c to use cloning ability. Else press f to move to the fight");
										String g5=s.next();
										if(g5.equals("c")) {
											c=c+1;
											sk21=sk2.clone();
											sk22=sk2.clone();
											sk23=sk2.clone();
										}
									}
									
								}
								else if((g4.equals("yes"))&&(ob.sidekick_list.size()==0)) {
									System.out.println("Sorry! you have no sidekicks left");
									sk2=null;
								}
								else if(g4.equals("no")) {
									System.out.println("sidekick not chosen");
									sk2=null;
								}
								
								int q=(int)ob.h_hp;
								int q2=(int)clone.m_hp;
								boolean b1=true;
								//fight begins			
								while(b1!=false) {
									if(ob.h_hp<=0) {
										cur=graph.get(0);
										System.out.println("You lost try again from initial position..");
										ob.h_hp=100;
										ob.h_xp=0;
										ob.h_level=1;
										cs=0;
										if(sk2!=null) {
											sk2.armor=0;
											sk2.s_hp=100;
										}
										break;
									}
									System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
									if(ob.moves_count>=3)
										System.out.println("3)Special Attack");
									int t1=s.nextInt();
									if(t1==1) {
										ob.attack(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										//sidekick attack
										if(sk2!=null) {
											if(sk21!=null) {
												if((sk2.special_power==0)&&(sk2.className.equals("minion"))&&(sk2.death==0)){
													c=c+1;
													sk2.s_attack(clone);
													sk21.s_attack(clone);
													sk22.s_attack(clone);
													sk23.s_attack(clone);
													
												}
											}
											else {
												sk2.s_attack(clone);
											}	
										}
									}
									if(t1==2) {
										if((sk2!=null)&&(clone.m_level==2)&&(sk2.className.compareTo("knight")==0)) {
											System.out.println("Defence mechanism of knight is in use.");
											ob.def_sidekick=5;
										}
										ob.defense(clone);
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										
										System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									if(t1==3) {
										ob.superpower(clone);
										ob.moves_count=0;
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									
									}
									clone.harm(ob);      //monster attacks here
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
									System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
									//////////////////
									if(sk2!=null) {
										if(sk21!=null) {
											if((sk2!=null)&&(sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
												if((sk21.death==0)&&(sk22.death==0)&&(sk23.death==0)) {
													clone.harm_sidekick(sk21);
													clone.harm_sidekick(sk22);
													clone.harm_sidekick(sk23);
												}
											}
											else {
												sk2.death=1;
												sk2.s_hp=0;
												if(sk21!=null) {
													sk21.death=1;
													sk22.death=1;
													sk23.death=1;
													sk21.s_hp=0;
													sk22.s_hp=0;
													sk23.s_hp=0;
												}
											}
										}
										else {
											if((sk2.death==0)&&(sk2.s_hp>0)) {
												clone.harm_sidekick(sk2);
											}
										}
										if(sk2.s_hp<=0) {
											sk2.death=1;
											System.out.println("sidekick died");
											if(sk21!=null) {
												sk21.death=1;
												System.out.println("sidekick died");
												sk22.death=1;
												System.out.println("sidekick died");
												sk23.death=1;
												System.out.println("sidekick died");
											}
										}
									}
									
									/////////////////
									if(clone.m_hp<=0) {
										int xp=clone.m_level*20;
										
										System.out.println("Monster killed\n"+xp+" xp awarded");
										if(sk2!=null) {
											System.out.println("sidekick gained "+xp/10+" xp");
											sk2.s_xp=sk2.s_xp+xp/10; 
											if(sk2.s_xp%5==0) {
												sk2.s_attackfield+=1;
											}
											sk2.s_hp=100;
										}
										System.out.println("Fight won ! proceeding to next location");
										System.out.println("If you would like to buy a sidekick, type yes. Else no to upgrade level.");
										String g=s.next();
										if(g.equals("yes")) {
											System.out.println("Your current xp is : "+clone.m_level*20);
											System.out.println("If you want to buy a minion, press 1\n"+"If you want to buy a knight, press 2");
											int g2=s.nextInt();
											sidekick sk=null;
											if(g2==1) 
												sk=new minion();
											else if(g2==2) 
												sk=new knight();
											System.out.print("xp to spend : ");
											int g3=s.nextInt();
											System.out.println("You bought a sidekick "+sk.className);
											if(g2==1)
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-5));
											else
												sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-8));
											System.out.println("xp of sidekick is "+sk.s_xp+"\n"+"Attack of sidekick is "+sk.s_attackfield);
											ob.sidekick_list.add(sk);
											xp=xp-g3;
											
											
										}
										ob.h_xp+=xp;
										ob.h_hp=100;
										if((ob.h_xp>=20)&&(ob.h_xp<60))
											ob.h_level=2;
										if((ob.h_xp>=60)&&(ob.h_xp<120))
											ob.h_level=3;
										if((ob.h_xp>=120))
											ob.h_level=4;
										System.out.println("Level = level "+ob.h_level);
										if(ob.h_level==1) {
											ob.h_hp=100;
										}
										if(ob.h_level==2) {
											ob.h_hp=150;
										}
										if(ob.h_level==3) {
											ob.h_hp=200;
										}
										if(ob.h_level==4) {
											ob.h_hp=250;
										}
										if((c!=0)&&(sk2.special_power==0)) {
											sk2.special_power=1;
										}
										
										ob.def_sidekick=0;
										break;
									}
									if(ob.h_hp>q) {
										ob.h_hp=q;
									}
									if(clone.m_hp<0) {
										clone.m_hp=0;
									}
									if(ob.h_hp<0) {
										ob.h_hp=0;
									}
									
								}	
								if(sk2!=null) {
									if(ob.sidekick_list.get(0).death==1) {
										ob.sidekick_list.remove(sk2);
									}
								}
							}
							if(k3==4) {
								cur=cur.getBack_n();
								if(cur.getM()==null) {
									System.out.println("There is no monster here");
								}
								else {
									//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
									//back
									System.out.println("Moving to "+cur.getNumber());
									//cloning of monster
									monster clone=null;
									if(cur.getM().m_level==1) {
										clone=new goblin();
									}
									else if(cur.getM().m_level==2) {
										clone=new zombie();
									}
									else if(cur.getM().m_level==3) {
										clone=new fiends();
									}
									System.out.println("Fight started! You are fighting Level "+clone.m_level+" monster");
									//fighting a monster
									int c=0;
									ob.sort();
									System.out.println("Type yes if you want to use a side kick. Else type no.");
									String g4=s.next();
									sidekick sk2=null;
									
									//do part of comparison here change this null
									sidekick sk21=null;
									sidekick sk22=null;
									sidekick sk23=null;
									if(g4.equals("yes")&&(ob.sidekick_list.size()!=0)) {
										sk2=ob.sidekick_list.get(0);
										System.out.println("You have a sidekick "+sk2.className+" with you.Attack of sidekick is"+sk2.s_attackfield);
										if((sk2.special_power==0)&&(sk2.className.equals("minion"))) {
											System.out.println("Press c to use cloning ability. Else press f to move to the fight");
											String g5=s.next();
											if(g5.equals("c")) {
												c=c+1;
												sk21=sk2.clone();
												sk22=sk2.clone();
												sk23=sk2.clone();
											}
										}
										
									}
									else if((g4.equals("yes"))&&(ob.sidekick_list.size()==0)) {
										System.out.println("Sorry! you have no sidekicks left");
										sk2=null;
									}
									else if(g4.equals("no")) {
										System.out.println("sidekick not chosen");
										sk2=null;
									}
									
									int q=(int)ob.h_hp;
									int q2=(int)clone.m_hp;
									boolean b1=true;
									//fight begins			
									while(b1!=false) {
										if(ob.h_hp<=0) {
											cur=graph.get(0);
											System.out.println("You lost try again from initial position..");
											ob.h_hp=100;
											ob.h_xp=0;
											ob.h_level=1;
											cs=0;
											if(sk2!=null) {
												sk2.armor=0;
												sk2.s_hp=100;
											}
											break;
										}
										System.out.println("Choose move\n"+"1)Attack\n"+"2)Defense");
										if(ob.moves_count>=3)
											System.out.println("3)Special Attack");
										int t1=s.nextInt();
										if(t1==1) {
											ob.attack(clone);
											if(ob.h_hp>q) {
												ob.h_hp=q;
											}
											if(clone.m_hp<0) {
												clone.m_hp=0;
											}
											if(ob.h_hp<0) {
												ob.h_hp=0;
											}
											System.out.println("You choose to attack\n"+"You attacked and inflicted "+ob.h_damage+" damage to the monster.\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
											//sidekick attack
											if(sk2!=null) {
												if(sk21!=null) {
													if((sk2.special_power==0)&&(sk2.className.equals("minion"))&&(sk2.death==0)){
														c=c+1;
														sk2.s_attack(clone);
														sk21.s_attack(clone);
														sk22.s_attack(clone);
														sk23.s_attack(clone);
														
													}
												}
												else {
													sk2.s_attack(clone);
												}	
											}
										}
										if(t1==2) {
											if((sk2!=null)&&(clone.m_level==2)&&(sk2.className.compareTo("knight")==0)) {
												System.out.println("Defence mechanism of knight is in use.");
												ob.def_sidekick=5;
											}
											ob.defense(clone);
											if(ob.h_hp>q) {
												ob.h_hp=q;
											}
											if(clone.m_hp<0) {
												clone.m_hp=0;
											}
											if(ob.h_hp<0) {
												ob.h_hp=0;
											}
											
											System.out.println("You choose to defend\n"+"Monster attack reduced by "+ob.h_defend+"\n"+"Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										
										}
										if(t1==3) {
											ob.superpower(clone);
											ob.moves_count=0;
											if(ob.h_hp>q) {
												ob.h_hp=q;
											}
											if(clone.m_hp<0) {
												clone.m_hp=0;
											}
											if(ob.h_hp<0) {
												ob.h_hp=0;
											}
											System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										
										}
										clone.harm(ob);      //monster attacks here
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										System.out.println("Monster attack!\r\n"+"The monster attacked and inflicted "+clone.m_damage+" damage to you.");
										System.out.println("Your Hp: "+ob.h_hp+"/"+q+" Monsters Hp: "+clone.m_hp+"/"+q2+"");
										//////////////////
										if(sk2!=null) {
											if(sk21!=null) {
												if((sk2!=null)&&(sk2.death==0)&&(sk2.s_hp>0)) {
													clone.harm_sidekick(sk2);
													if((sk21.death==0)&&(sk22.death==0)&&(sk23.death==0)) {
														clone.harm_sidekick(sk21);
														clone.harm_sidekick(sk22);
														clone.harm_sidekick(sk23);
													}
												}
												else {
													sk2.death=1;
													sk2.s_hp=0;
													if(sk21!=null) {
														sk21.death=1;
														sk22.death=1;
														sk23.death=1;
														sk21.s_hp=0;
														sk22.s_hp=0;
														sk23.s_hp=0;
													}
												}
											}
											else {
												if((sk2.death==0)&&(sk2.s_hp>0)) {
													clone.harm_sidekick(sk2);
												}
											}
											if(sk2.s_hp<=0) {
												sk2.death=1;
												System.out.println("sidekick died");
												if(sk21!=null) {
													sk21.death=1;
													System.out.println("sidekick died");
													sk22.death=1;
													System.out.println("sidekick died");
													sk23.death=1;
													System.out.println("sidekick died");
												}
											}
										}
										
										/////////////////
										if(clone.m_hp<=0) {
											int xp=clone.m_level*20;
											
											System.out.println("Monster killed\n"+xp+" xp awarded");
											if(sk2!=null) {
												System.out.println("sidekick gained "+xp/10+" xp");
												sk2.s_xp=sk2.s_xp+xp/10; 
												if(sk2.s_xp%5==0) {
													sk2.s_attackfield+=1;
												}
												sk2.s_hp=100;
											}
											System.out.println("Fight won ! proceeding to next location");
											System.out.println("If you would like to buy a sidekick, type yes. Else no to upgrade level.");
											String g=s.next();
											if(g.equals("yes")) {
												System.out.println("Your current xp is : "+clone.m_level*20);
												System.out.println("If you want to buy a minion, press 1\n"+"If you want to buy a knight, press 2");
												int g2=s.nextInt();
												sidekick sk=null;
												if(g2==1) 
													sk=new minion();
												else if(g2==2) 
													sk=new knight();
												System.out.print("xp to spend : ");
												int g3=s.nextInt();
												System.out.println("You bought a sidekick "+sk.className);
												if(g2==1)
													sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-5));
												else
													sk.s_attackfield=sk.s_attackfield+(int)(0.5*(g3-8));
												System.out.println("xp of sidekick is "+sk.s_xp+"\n"+"Attack of sidekick is "+sk.s_attackfield);
												ob.sidekick_list.add(sk);
												xp=xp-g3;
												
												
											}
											ob.h_xp+=xp;
											ob.h_hp=100;
											if((ob.h_xp>=20)&&(ob.h_xp<60))
												ob.h_level=2;
											if((ob.h_xp>=60)&&(ob.h_xp<120))
												ob.h_level=3;
											if((ob.h_xp>=120))
												ob.h_level=4;
											System.out.println("Level = level "+ob.h_level);
											if(ob.h_level==1) {
												ob.h_hp=100;
											}
											if(ob.h_level==2) {
												ob.h_hp=150;
											}
											if(ob.h_level==3) {
												ob.h_hp=200;
											}
											if(ob.h_level==4) {
												ob.h_hp=250;
											}
											if((c!=0)&&(sk2.special_power==0)) {
												sk2.special_power=1;
											}
											
											ob.def_sidekick=0;
											break;
										}
										if(ob.h_hp>q) {
											ob.h_hp=q;
										}
										if(clone.m_hp<0) {
											clone.m_hp=0;
										}
										if(ob.h_hp<0) {
											ob.h_hp=0;
										}
										
									}	
									if(sk2!=null) {
										if(ob.sidekick_list.get(0).death==1) {
											ob.sidekick_list.remove(sk2);
										}
									}
									
								}
							}
							if(k3==-1) {
								cs=0;
								ob.h_level=1;
								ob.h_hp=100;
								ob.h_xp=0;
								 break;
							}
						}
							
					}
		
					
				}
				
				
				
			}
			else if(n==3) {
				
				a=false;
			}
			else {
				System.out.println("Wrong input! try again");
			}
			
		}

	}

}