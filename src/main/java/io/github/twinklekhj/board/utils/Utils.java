package io.github.twinklekhj.board.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Utils {
    public static String toString(Document doc, boolean outputDocType) {
        StringWriter writer = new StringWriter();
        Transformer transformer = null;

        try {
            transformer = TransformerFactory.newInstance().newTransformer();

            if (outputDocType) {
                DocumentType type = doc.getDoctype();
                if (type != null) {
                    String publicId = type.getPublicId();

                    if (publicId != null) {
                        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
                    }
                    String systemId = type.getSystemId();

                    if (systemId != null) {
                        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Could not create a transformer. message - {}", e.getMessage());
        }

        try {
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (Exception e) {
            log.error("Could not create a transformer. message - {}", e.getMessage());
        }

        return writer.toString();
    }

    public static boolean saveXml(String filename, Document doc) {
        try {
            DOMSource source = new DOMSource(doc);

            File file = new File(filename);
            StreamResult result = new StreamResult(file);

            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");

            xformer.transform(source, result);

            return true;
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Document createDomDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            return doc;
        } catch (Exception e) {
            log.error("Could not create a transformer. message: {}", e.getMessage());
        }

        return null;
    }

    public static Document parseXmlFile(String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(false);
            // factory.setNamespaceAware(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File(filename);

            if (!f.exists())
                return null;

            Document doc = builder.parse(f);

            return doc;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public static Document parseXmlString(String xml) {
        if (xml == null || xml.trim().length() < 5)
            return null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(false);
            // factory.setNamespaceAware(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.trim().getBytes(StandardCharsets.UTF_8)));

            return doc;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;

    }

    public static Element getChildElement(Element parent, String child_name) {
        NodeList child = parent.getChildNodes();
        Node node = null;
        Element elem = null;

        for (int i = 0; i < child.getLength(); i++) {
            node = child.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elem = (Element) node;

                if (child_name.equalsIgnoreCase(elem.getNodeName())) {
                    return elem;
                }
            }
        }

        return null;
    }

    public static List<Element> getChildElements(Element parent, String child_name) {
        NodeList child = parent.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        Node node = null;
        Element elem = null;

        for (int i = 0; i < child.getLength(); i++) {
            node = child.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elem = (Element) node;

                if (child_name.equals(elem.getNodeName())) {
                    list.add(elem);
                }
            }
        }

        return list;
    }

    public static List<Element> getChildElements(Element parent) {
        NodeList child = parent.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        Node node = null;
        Element elem = null;

        for (int i = 0; i < child.getLength(); i++) {
            node = child.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elem = (Element) node;
                list.add(elem);
            }
        }

        return list;
    }

    public static String getChildText(Element parent, String child_name) {
        Element child = getChildElement(parent, child_name);

        if (child == null)
            return null;

        return getText(child);
    }

    public static String getText(Element node) {
        StringBuilder sb = new StringBuilder();
        NodeList list = node.getChildNodes();
        Node nod;

        for (int i = 0; i < list.getLength(); i++) {
            nod = list.item(i);

            switch (nod.getNodeType()) {
                case Node.CDATA_SECTION_NODE:
                case Node.TEXT_NODE:
                    sb.append(nod.getNodeValue());
            }
        }

        return sb.toString();
    }

    public static boolean canAccess(String ip, Integer port) throws IOException {
        try (Socket socket = new Socket(ip, port)) {
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

    public static String getClientIP(HttpServletRequest request) {
        String[] headers = {"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR",
                "X-Real-IP", "X-RealIP", "REMOTE_ADDR"};
        String ip = request.getHeader("X-Forwarded-For");

        for (String header : headers) {
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    public static String getClientIPTest(HttpServletRequest request) {
        String ip = getClientIP(request);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("ip :" + ip);

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String strIpAdress = inetAddress.getHostAddress();

            System.out.println("inetAddress IP : " + strIpAdress);

            System.out.println("Inet4Address ip :" + Inet4Address.getLocalHost().getHostAddress());
            System.out.println("getCurrentEnvironmentNetworkIp ip :" + getCurrentEnvironmentNetworkIp(request));
            System.out.println("getIP ip :" + getIP(request));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }

    public static String getIP(HttpServletRequest request) throws UnknownHostException {
        String remoteHost = request.getRemoteHost();
        String remoteAddr = request.getRemoteAddr();

        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            InetAddress localIP = InetAddress.getLocalHost();
            remoteAddr = localIP.getHostAddress();
            remoteHost = localIP.getHostName();
        }
        int remotePort = request.getRemotePort();

        return String.format("%s(%s:%s)", remoteHost, remoteAddr, remotePort);
    }

    public static String getCurrentEnvironmentNetworkIp(HttpServletRequest request) {
        Enumeration<NetworkInterface> netInterfaces = null;

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return getLocalIp();
        }

        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();

            if (address == null) {
                return getLocalIp();
            }
            while (address.hasMoreElements()) {
                InetAddress addr = address.nextElement();
                if (!addr.isLoopbackAddress() && !addr.isSiteLocalAddress() && !addr.isAnyLocalAddress()) {
                    String ip = addr.getHostAddress();
                    if (ip.contains(".") && !ip.contains(":")) {
                        return ip;
                    }
                }
            }
        }
        return getLocalIp();
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static int getEndDay(String startDate) {
        Calendar cal = Calendar.getInstance();
        if (!startDate.equals("")) {
            cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)) - 1, 01);
        }
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Instant getLocalDateTime(Date date) {
        return date.toInstant();
    }

    public static Instant getNextLocalDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return cal.toInstant();
    }

    public static String getNextDate(String startDate, String gubun) {

        String rtnVal = "";

        if (!startDate.equals("")) {
            if (gubun.equals("Day")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)) - 1,
                        Integer.parseInt(startDate.substring(8, 10)));
                cal.add(Calendar.DATE, 1); // 다음달
                rtnVal = df.format(cal.getTime());
            } else if (gubun.equals("Month")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)) - 1,
                        01);
                cal.add(Calendar.MONTH, 1); // 다음달

                rtnVal = df.format(cal.getTime());
            } else if (gubun.equals("Year")) {
                DateFormat df = new SimpleDateFormat("yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(startDate.substring(0, 4)), 01, 01);
                cal.add(Calendar.YEAR, 1); // 다음년도

                rtnVal = df.format(cal.getTime());
            }
            return rtnVal;
        } else
            return "";
    }

    public static Set<String> getSetFromStr(String str) {
        String[] arr = str.split(",");
        Stream<String> stream = Arrays.stream(arr).map(String::trim).filter(item -> !item.equals(""));
        return stream.collect(Collectors.toSet());
    }

    public static SortedSet<String> getSortedSet() {
        SortedSet<String> set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                int val1 = ParamUtil.parseInt(obj1);
                int val2 = ParamUtil.parseInt(obj2);

                // 0을 반환하지 않을 경우 삭제 안됨.
                return Integer.compare(val1, val2);
            }
        });

        return set;
    }
}
