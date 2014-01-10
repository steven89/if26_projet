package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

public class BsonEcho extends Echo {

	public BsonEcho(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void echo(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void echo(BSONObject obj) {
		// TODO Auto-generated method stub
		//BasicBSONObject bson =  (BasicBSONObject) JSON.parse("{'test': 'aaeaze', 't':'a'}");
				//byte[] bte = BSON.encode(bson);	
				/*OutputStream os = response.getOutputStream();
				os.write(bte);
				os.flush();*/
				/*PrintWriter out = response.getWriter();
				String str = "";
				for(byte b : bte){
					str+=b+"&";
					//out.print(b+"\n");
				}
				out.print(str);
				
//				String decode = new String(bte, "UTF-8");
//				out.print("\n"+decode.toString());
				String[] tab = str.split("&");
				byte[] array = new byte[tab.length];
				for(int i =0; i<tab.length; i++){
//					out.print("\n"+tab[i]);
					array[i] = Byte.valueOf(tab[i]);
				}
				BSONDecoder decoder = new BasicBSONDecoder();
				BasicBSONObject obj = (BasicBSONObject) decoder.readObject(array);
				//out.print(obj.toString());*/
	}

}
