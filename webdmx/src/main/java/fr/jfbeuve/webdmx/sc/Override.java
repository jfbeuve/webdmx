package fr.jfbeuve.webdmx.sc;

public class Override {
	public RGBFixtureState[] override;
	public int[] reset;
	public long fade = 0;
	public int layer=1;
	/* {
	 *   override: [
	 *   	{id:1, r:255, g:0, b:0, dim:100, strob:0}
	 *   	{id:2, r:-1, g:-1, b:-1, dim:100, strob:0}
	 *   ],
	 *   reset: [3,4],
	 *   fade: 0,
	 *   layer: 1
	 * }
	 */
	public Override(){
		reset = new int[0];
	}
	
	public Override(Scene scene, int[] _reset, int _layer){
		override=scene.fixtures;
		reset=_reset;
		fade=scene.fade;
		layer=_layer;
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append("OVERRIDE LAYER "+layer+" SET ");
		for(int i=0;i<override.length;i++)
			s.append(override[i]);
		if(reset.length>0) s.append(" RESET ");
		for(int i=0;i<reset.length;i++)
			s.append((i>0?",":"")+reset[i]);
		s.append(" fade="+fade);
		return s.toString();
	}
}
