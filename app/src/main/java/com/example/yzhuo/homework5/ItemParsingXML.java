package com.example.yzhuo.homework5;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by yzhuo on 10/17/2015.
 */
public class ItemParsingXML {
    static public class PodcastParser extends DefaultHandler{
        ArrayList<Item> itemArrayList;
        Item item;
        StringBuilder xmlInnerText;
        boolean initem = false;

        static public ArrayList<Item> itemsXMLArrayList(InputStream in) throws IOException, SAXException{
            PodcastParser parser = new PodcastParser();
            Xml.parse(in, Xml.Encoding.UTF_8, parser);
            return parser.getItemList();
        }

        private ArrayList<Item> getItemList() {
            return itemArrayList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            xmlInnerText = new StringBuilder();
            itemArrayList = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("item")){
                initem = true;
                item = new Item();
            } else if(localName.equals("image")){
                if(initem) {
                    item.setImageURL(attributes.getValue("href"));
                    Log.d("imageURL", item.getImageURL());
                }
            } else if(localName.equals("enclosure")){
                if(initem) {
                    item.setMediaURL(attributes.getValue("url"));
                    Log.d("MP3 File", item.getMediaURL());
                }
            }
            xmlInnerText.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(localName.equals("item")){
                if(initem) {
                    itemArrayList.add(item);
                    //Log.d("Item added", item.toString());
                }
            }  else if(localName.equals("title")){
                if(initem) {
                    item.setTitles(xmlInnerText.toString().trim());
                    Log.d("Title", item.getTitles());
                }
            } else if(localName.equals("pubDate")){
                if(initem) {
                    String temp = xmlInnerText.toString().trim();
                    String[] part = temp.split(" ");
                    String result = part[0] + " " + part[1] + ", " + part[2] + " " + part[3];
                    item.setReleaseDate(result);
                    Log.d("Date", item.getReleaseDate());
                }
            }  else if(localName.equals("summary")){
                if(initem) {
                    item.setDescription(xmlInnerText.toString().trim());
                    //Log.d("Summary", item.getDescription());
                }
            } else if(localName.equals("duration")){
                if(initem) {
                    item.setDuration(xmlInnerText.toString().trim());
                    Log.d("Duration", item.getDuration());
                }
            }

            xmlInnerText.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            xmlInnerText.append(ch,start,length);
        }
    }
}
