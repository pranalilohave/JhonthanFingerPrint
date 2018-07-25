package com.fgtit.fpcore;

public class FPMatch {

	private static FPMatch mMatch=null;

	public static FPMatch getInstance(){
		if(mMatch==null){
			mMatch=new FPMatch();
		}
		return mMatch;
	}
//2136==(b&f)
	//c=200

	public native int InitMatch();
	public native int MatchTemplate( byte[] piFeatureA, byte[] piFeatureB);

	static {
		System.loadLibrary("fgtitalg");
		System.loadLibrary("fpcore");
	}
}
