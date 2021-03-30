package git01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class makeKeyword {
	


	
	static void make_Keyword(String xmlLocation) throws ParserConfigurationException, SAXException, IOException, TransformerException { //xmlLocation에는 collection.xml의 경로가 포함되어있음
		
	
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		
		File collection_xml=new File(xmlLocation);
		Document document=builder.parse(collection_xml); 
		document.getDocumentElement().normalize();
		
		NodeList foodList=document.getElementsByTagName("doc"); //bodyTag개수는 5개(food 개수만큼)
		
		String bodyContent="";
		

		
		for(int i=0;i<foodList.getLength();i++) { //5개
			
		Node nodeItem=foodList.item(i);
		Element element = (Element)nodeItem;
		String newbody = "";

		
		bodyContent=element.getElementsByTagName("body").item(0).getTextContent();
		KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(bodyContent, true);
		for(int j=0;j<kl.size();j++) {
			Keyword kwrd=kl.get(j);
			newbody+=kwrd.getString()+":"+kwrd.getCnt()+"#";	
			//KeywordHashmap.put(kwrd.getString(), kwrd.getCnt());

			}
		
		element.getElementsByTagName("body").item(0).setTextContent(newbody);
	
		}
	
		TransformerFactory transformerFactory= TransformerFactory.newInstance();
		Transformer transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		
		
		DOMSource source = new DOMSource(document);
		StreamResult result=new StreamResult(new FileOutputStream(new File("C:\\Users\\Hyeonji\\Desktop\\4-1\\OpenSource/index.xml")));
		transformer.transform(source, result);
	
	}
}
