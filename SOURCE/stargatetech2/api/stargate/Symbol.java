package stargatetech2.api.stargate;

public enum Symbol {
	VOID(""),
	AT	("At"),
	AL	("Al"),
	CLA	("Cla"),
	UR	("Ur"),
	ON	("On"),
	DEH	("Deh"),
	EC	("Ec"),
	MIG	("Mig"),
	ALM	("Alm"),
	RUM	("Rum"),
	AR	("Ar"),
	VA	("Va"),
	COR	("Cor"),
	PRA	("Pra"),
	OM	("Om"),
	ET	("Et"),
	AS	("As"),
	US	("Us"),
	GON	("Gon"),
	ORM	("Orm"),
	EM	("Em"),
	AC	("Ac"),
	OTH	("Oth"),
	LOS	("Los"),
	LAN	("Lan"),
	EST	("Est"),
	CRO	("Cro"),
	SIL	("Sil"),
	TA	("Ta"),
	BREI("Brei"),
	RUSH("Rush"),
	ERP	("Erp"),
	SET	("Set"),
	ULF	("Ulf"),
	PRO	("Pro"),
	SAL	("Sal"),
	TIS	("Tis"),
	MAC	("Mac"),
	IRT	("Irt");
	
	private String name;
	
	private Symbol(String name){
		this.name = name;
	}
	
	public static Symbol get(int s){
		if(s >= 0 && s <= 39){
			return values()[s];
		}else{
			return VOID;
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
}