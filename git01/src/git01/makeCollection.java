package git01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
//import org.jsoup.select.Elements;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

public class makeCollection {
	
	
	
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
		// TODO Auto-generated method stub
		
		
		DocumentBuilderFactory docFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document doc=docBuilder.newDocument();
		
		Text lineBreak = doc.createTextNode("\n");
		
		org.w3c.dom.Element docs=doc.createElement("docs");
		doc.appendChild(docs);
		
		
		
		String[] food= {"떡","라면","아이스크림","초밥","파스타"};
		
		for(int i=0;i<food.length;i++) {
			
		File input=new File("C:\\Users\\Hyeonji\\Desktop\\4-1\\OpenSource\\week2_html\\"+food[i]+".html");

		org.jsoup.nodes.Document docu=Jsoup.parse(input,"UTF-8");
		Elements name=docu.getElementsByTag("title");
		Elements info=docu.getElementsByTag("p");
		
		

		
		org.w3c.dom.Element docc=doc.createElement("doc");
		docs.appendChild(docc);
		docc.setAttribute("id", String.valueOf(i));
		docc.appendChild(lineBreak);
		
		org.w3c.dom.Element title=doc.createElement("title");
		title.appendChild(doc.createTextNode(name.text()));
		docc.appendChild(title);
		docc.appendChild(lineBreak);
		
		org.w3c.dom.Element body=doc.createElement("body");
		body.appendChild(doc.createTextNode(info.text()));
		docc.appendChild(body);
		docc.appendChild(lineBreak);
		
		}
	
		
		TransformerFactory transformerFactory= TransformerFactory.newInstance();
		Transformer transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result=new StreamResult(new FileOutputStream(new File("C:\\Users\\Hyeonji\\Desktop\\4-1\\OpenSource/collection.xml")));
		transformer.transform(source, result);
		

		
	}

}
