package git01;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	
	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	
		if(args[0].equals("-c")) makeCollection.make_Collection(args[1]);
		if(args[0].equals("-k")) makeKeyword.make_Keyword(args[1]);
		if(args[0].equals("-i")) indexer.make_indexPost(args[1]);
		if(args[0].equals("-s")) searcher.search_query(args[1],args[2],args[3]); 
	}
}
