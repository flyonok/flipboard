package sinaNews;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

// <summary>
// 自动获取频道列表类
// </summary>
public class OutlineManager {
	
	// <summary>
    // 获取频道列表，包含子节点
    // </summary>
    // <param name="xmlUrl">根频道地址</param>
    // <returns></returns>
	public List<OutLine> getCannels(String xmlUrl)
	{
		List<OutLine> newsList = new ArrayList<OutLine>();
		try {
			SAXReader saxReader = new SAXReader();
        	Document document = saxReader.read(new URL(xmlUrl));
        	List<?> list = document.selectNodes("//body/outline" );
        	Iterator<?> iter=list.iterator();
        	while(iter.hasNext()){
                
                OutLine outline = new OutLine();
                Element child =(Element)iter.next();
                Attribute attr = child.attribute("title");
                if (attr != null)
                	outline.setTitle(attr.getValue().trim());
                attr = child.attribute("text");
                if (attr != null )
                	outline.setText(attr.getValue().trim());
                attr = child.attribute("type");
                if (attr != null )
                	outline.setType(attr.getValue().trim());
                attr = child.attribute("xmlUrl");
                if (attr != null)
                	outline.setXmlUrl(attr.getValue().trim());
                attr = child.attribute("htmlUrl");
                if (attr != null)
                	outline.setHtmlUrl(attr.getValue().trim());
                System.out.println("title:" + outline.getTitle());
                System.out.println("xmlUrl:" + outline.getXmlUrl());
                addChildElements(child, outline);
                newsList.add(outline);
        	}
		}catch(MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// return null;
		}catch(DocumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// return null;
		}
		return newsList;
	}
	
	private void addChildElements(Element xNode, OutLine ol)
    {
        if (xNode == null) return;

        Iterator<?> iterator = xNode.elementIterator("outline");
       // 递归，添加子节点
        while(iterator.hasNext()){
        
            OutLine outline = new OutLine();
            Element child =(Element)iterator.next();
            Attribute attr = child.attribute("title");
            if (attr != null)
            	outline.setTitle(attr.getValue().trim());
            attr = child.attribute("text");
            if (attr != null )
            	outline.setText(attr.getValue().trim());
            attr = child.attribute("type");
            if (attr != null )
            	outline.setType(attr.getValue().trim());
            attr = child.attribute("xmlUrl");
            if (attr != null)
            	outline.setXmlUrl(attr.getValue().trim());
            attr = child.attribute("htmlUrl");
            if (attr != null)
            	outline.setHtmlUrl(attr.getValue().trim());
           
            ol.childrenOutline().add(outline);
            System.out.println("child:\t");
            System.out.println("title:" + outline.getTitle());
            System.out.println("xmlUrl:" + outline.getXmlUrl());

            addChildElements(child, outline);
        }
    }

}
