package git01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class searcher {
	
		static List<String> keywordlist=new ArrayList<String>();
		static List<Integer> wq=new ArrayList<Integer>();
		static List<Double> Qidt_array = new ArrayList<Double>();
		

	static void search_query(String postLocation,String args2, String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		
		
		if(args2.equals("-q")) {
			KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(query, true);
		for(int i=0;i<kl.size();i++) {
			Keyword kwrd=kl.get(i);
			String keyword=kwrd.getString(); //keyword : ������ �� Ű����
			int keyWeight=kwrd.getCnt(); //keyWeight : �� Ű������ weight
			keywordlist.add(keyword);
			wq.add(keyWeight);
		}
		}

		
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		
		File collection_xml=new File("C:\\Users\\Hyeonji\\Desktop\\4-1\\OpenSource\\collection.xml");
		Document document=builder.parse(collection_xml); 
		document.getDocumentElement().normalize();
	
		NodeList foodList=document.getElementsByTagName("doc"); //bodyTag������ 5��(food ������ŭ)
		
		
		
		FileInputStream fileStream=new FileInputStream(postLocation);
		ObjectInputStream objectInputStream=new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		HashMap<String,List<Double>> hashMap=(HashMap)object;
		
		
		HashMap<Integer,Double> Qidt_map = new HashMap<Integer,Double>();

		for(int i=0;i<foodList.getLength();i++) { //�������� ������ �ٲ�ߵ�
		
			
			Qidt_map.put(i,InnerProduct(hashMap,i));
		}
		
		
		List<Integer> keySetList = new ArrayList<>(Qidt_map.keySet());
		Collections.sort(keySetList, (o1, o2) -> (Qidt_map.get(o2).compareTo(Qidt_map.get(o1))));
		int cnt=0;
		for(Integer key : keySetList) {
//			//System.out.println("key : " + key + " / " + "value : " + Qidt_map.get(key));
//			//doc id�� key �ΰ��� title �����ͼ� ���
			if(cnt==3)break;
			for(int i=0;i<foodList.getLength();i++) {
				Element element=(Element)foodList.item(i);
				String id=element.getAttribute("id");
				int id_num=Integer.parseInt(id);
				if(id_num==key) {
					cnt++;
					System.out.println(element.getElementsByTagName("title").item(0).getTextContent());
					break;
				}
				
			}
		

		}
		
		
		
		
	
	}
	
	
	
	static double InnerProduct(HashMap<String,List<Double>> hashMap,int i) {
		double Qidt=0;
		double x=0;
		for(int j=0;j<keywordlist.size();j++) { //���,��,�и�,���� (4���� Ű���忡 ���ؼ�)
			Iterator<String> it=hashMap.keySet().iterator();
			while(it.hasNext()) {
				String key=it.next();
				if(key.equals(keywordlist.get(j))){ //Ű���带 index.post�� key�� ��
					List<Double> value=hashMap.get(key); 
					for(int k=0;k<value.size();k++) {
						if((value.get(k)).equals((double)i)){
							x=value.get(k+1);
							break;
						}
						else x=0;
					}
					
				}
				
			}
			Qidt+=(wq.get(j))*x;
			//System.out.print("+("+(wq.get(j))+"x"+x+")");
			
		}
		return Math.round(Qidt*100)/100.0;
		
	}
}