package git01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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

public class indexer {
	
	public static List<String> get_keywordlist(String xmlLocation) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		File index_xml=new File(xmlLocation);
		Document document=builder.parse(index_xml); 
		document.getDocumentElement().normalize(); //index.xml 불러옴
		NodeList foodList=document.getElementsByTagName("doc");  //foodList.getLength()= 5(문서개수)
		
		List<String> keyword_list=new ArrayList<String>();
		for(int i=0;i<foodList.getLength();i++) { 
			Node nodeItem=foodList.item(i);
			Element element = (Element)nodeItem;
			String body=element.getElementsByTagName("body").item(0).getTextContent();
			
			String[] body_split=body.split("#");
			
			for(int j=0;j<body_split.length;j++) {
				String data=body_split[j];
				String[] split_twice=data.split(":");
				keyword_list.add(split_twice[0]);
				
			}
	}
		return keyword_list;
	}
	
	public static int calcDFX(List<String> keyword_list,String keyword) {
		int dfx=0;
		
		for(int i=0;i<keyword_list.size();i++) {
			if(keyword_list.get(i).equals(keyword)){
				dfx++;
			}
		}
		
		return dfx;
	}

	
	public static double calcTFIDF(int tfXY,int N,double dfX) {
		double wXY;
		
		double calc_wXY=tfXY*Math.log(N/dfX);
		wXY=Math.round(calc_wXY*100)/100.0;
		
		
		return wXY;
		
	}
	
	
	static void make_indexPost(String xmlLocation) throws IOException, ParserConfigurationException, SAXException {
		
		//1. index.xml불러오기
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		
		File index_xml=new File(xmlLocation);
		Document document=builder.parse(index_xml); 
		document.getDocumentElement().normalize(); //index.xml 불러옴
		
		NodeList foodList=document.getElementsByTagName("doc");  //foodList.getLength()= 5(문서개수)
		
		List<String> keyword_list=get_keywordlist(xmlLocation);
		
		FileOutputStream fileStream = new FileOutputStream("C:\\Users\\Hyeonji\\Desktop\\4-1\\OpenSource\\index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		HashMap<String,List<Double>> map=new HashMap<String,List<Double>>();
		
		
		int N=foodList.getLength(); //N=5 (문서개수)
		String bodyContent="";

		for(int i=0;i<foodList.getLength();i++) { 
		Node nodeItem=foodList.item(i);
		Element element = (Element)nodeItem;
		

		bodyContent=element.getElementsByTagName("body").item(0).getTextContent();
		
		String keyword = "";
		int tfXY;
		String[] strAry=bodyContent.split("#");
		for(int j=0;j<strAry.length;j++) { //하나의 문서안에 있는 키워드 수만큼
			String data=strAry[j];
			String[] keyValue=data.split(":");
			keyword=keyValue[0];
			tfXY=Integer.parseInt(keyValue[1]); //키워드랑 그 문서 내 빈도수 구함
			
			double dfx=calcDFX(keyword_list,keyword); //dfx : 몇개의 문서에서 키워드가 등장하는지
			double tfidf=calcTFIDF(tfXY,N,dfx); //tfidf: 가중치 값

	
			
		List<Double> list=new ArrayList<Double>();
		
		if(tfidf!=0) {
		list.add((double) i);
		list.add(tfidf);
		

		if(map.containsKey(keyword)) {
			List<Double> valuelist=map.get(keyword);
			valuelist.add((double)i);
			valuelist.add(tfidf);
			map.put(keyword, valuelist);
		}
		else
			map.put(keyword,list);
		}
		}
		}
		
		
		
		objectOutputStream.writeObject(map);
		objectOutputStream.close();
		
		

		

		

}
}
