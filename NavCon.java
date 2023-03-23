
package netnav;

import javax.annotation.processing.Messager;
import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.wordgraph.HyperTree.HyperTree;
import com.wordgraph.HyperTree.Line;

import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.*; // to get .Graph, .Edge which conflict
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
//import runner.TraversalExample.Rels;
//import runner.TraversalExample;

import net.nettree.Net;
import net.nettree.NetTreeException;
import net.nettree.Netprop;
import net.nettree.Node;
import net.nettree.Nprop;
import net.nettree.Link;
import net.nettree.Lprop;
import net.nettree.Unet2Net;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;

import org.apache.xerces.parsers.DOMParser;
import org.jfree.util.Log;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.*; // to get Node not in conflict with wg 
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.cypher.result.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.helpers.collection.Iterators;
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.Result;

import javax.xml.transform.dom.DOMResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

/**
 * NavCon is an implementation of NetTree(tm) technology to provide for
 * multidimensional Navigation and Control of various resources, objects 
 * 
 * ToDos:
 * . get netsetting working ! ! ! get line elements in the DOM
 * . Change ... to a popup overlay type window ????
 * . collapse all, collapse to path
 * . take out obsolete code, (most of the html and all neo4j
 * . send properties (all kinds:global, interface, ...)  in sendLinePopup so it is populated correctly
 * . get messages out to all windows
 * . netnav.Unet2Net <- net.nettree.Unet2Net back to normal
 * 
 *
 * 
 * 20201016 setNetTreeControl // sendNormalBegin
 * 20211108 setNetSettings // sendNormalBegin
 */

@SuppressWarnings(value = "all")
public class NavCon implements Serializable, Service {
	private static final long serialVersionUID = 6463715914360860703L;
	String version = "20211108 setNetSettings // sendNormalBegin ";
	PrintWriter out = null;
	int timeToLive = 0;
	public String status = "created";
	String name = "NavCon";
	Properties position = new Properties();
	Line linehold = null;
	NetLine netlinehold = null;
	NetTree nettreeA = null;

	net.nettree.NetTree unettree = null;
	long longer = 1000;
	HashSet<String> nodeset = new HashSet<String>();
	int intlineno = 0;
	// these will be set from request.getParameter()
	String fx = "";
	String fx1 = "";
	String URL = "";
	//String templetURL = "";
	//String netpropsURL = "";
	String file = "";
	String node = "";
	String lineno = "";
	String focus = "";
	String maxlevel = "";
	String mapname = "";
	String sourcetype = "";
	String graphname = "";
	String graphid = "";
	String firstnodename = "";
	String fileformat = "";
	String stringA = "";
	String internalid = "";
	String linkinternalid = "";
	String Name = "";
	String id = "";
	boolean sourcetypenewxml = false;
	String sqlstring = "";
	String sqlselectstring = "";
	String sqlfromstring = "";
	String sqlwherestring = "";
	
	HttpServletRequest request;
	HttpServletResponse response;
	ServletContext servletcontextA = null;
	int outlinematches = 0;
	int bigdecimal;
	int intA = 0;
	Connection connectionB = null;
	HttpSession session = null;
	UserSession usersession = null;
	Statement statementA = null;
	ResultSet resultsetA = null;
	StringBuffer sbA = null;
	//GraphSourceXMLURL gxs = null;
	Class classA;
	Method methodA;
	Page pageA = null;
	String title = "";
	GraphDatabaseFactory neographDbFactory = null;
	GraphDatabaseService neographdbworking = null;
	org.neo4j.graphdb.Node neonodework = null;
	org.neo4j.graphdb.Node neonodeworkLeft = null;
	org.neo4j.graphdb.Node neonodeworkRight = null;
	org.neo4j.graphdb.Node nodeinlist = null;
	org.neo4j.graphdb.Relationship neorelationshipwork = null;
	org.neo4j.graphdb.RelationshipType RelType = null;
	org.neo4j.graphdb.Label neolabelA = null;
	Transaction neographworktx = null;
	private enum ExampleTypes implements RelationshipType {	MY_TYPE }
	private TraversalDescription friendsTraversal;
	private enum Rels implements RelationshipType {	LIKES, KNOWS }

	private static int

	PLUS = 1, MINUS = 2;	
	/*
	 * FOCUS = -1, BLANK =0, LINEUP = 1, LINEDOWN = 2, FIRSTUP = 3, LASTDOWN = 4,
	 * TEEUP = 5, TEEDOWN = 6, HYPER = 1, HYPO = 2, LINEDRAW = 0, HIGHLIGHT = 2, YES
	 * = 1, LINKTYPE = 1, NO = 0
	 */
    enum MyRels implements RelationshipType { LINK  }
	String tempfilename = "";
	String tempfileurl = "";
	Iterator<GenProp> propiter;
	Iterator<String> keysetiter;
	GenProp genprophold;
	GenPropManager  genpropman = null;
	String cnetfile = "http://localhost/default.cnet";

	public NavCon(UserSession usersessionI, HttpServletRequest request) throws ServiceException {
		try {
			this.usersession = usersessionI;
			genpropman = usersession.genpropman;
			session = request.getSession(true);
			status = "New";
			this.sbA = new StringBuffer();
			FileUtils.deleteDirectory(new File("data/working"));
			GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
			neographdbworking = graphDbFactory.newEmbeddedDatabase(new File("data/working"));
			usersession.neographdbworking = neographdbworking;
			this.setNetTreeControl();
		} catch (java.lang.RuntimeException re ) {
			usersession.log.error("NavCon:constructor:re:" + usersession.stack2string(re));
		} catch (Exception e) {
			usersession.log.error("NavCon:constructor:e:" + usersession.stack2string(e));
			throw new ServiceException(this.usersession, request, "NavCon:constructor:e:" + e.toString(), e);
		}
		servletcontextA = usersession.servletcontextA;
		usersession.log.debug("NavCon:constructor:version:" + version);
		usersession.log.debug(
				"NC:constructor:after newEmbeddedDatabase:available:" + neographdbworking.isAvailable(longer));
	} // NavCon() constructor

	/**
	 * The fx parameter value is the name of the method to be performed.
	 */
	public void interpreter(UserSession usersession, HttpServletRequest request, HttpServletResponse responseI)
			throws ServiceException {
		usersession.log.debug("NC:11111111111111111111111111111111111111111111");
		usersession.log.debug("NC:interperter:version:" + this.version + " size:"  + usersession.nettreecontrol.linespecsethash.size());  
		usersession.log.debug("NC:11111111111111111111111111111111111111111111");
		this.pageA = (Page) usersession.getService("Page");
		session = request.getSession(true);
		fx = request.getParameter("fx");
		focus = request.getParameter("focus");
		lineno = request.getParameter("lineno"); 
		mapname = request.getParameter("mapname");
		node = request.getParameter("node");
		file = request.getParameter("file");
		graphname = request.getParameter("graphname");
		graphid = request.getParameter("graphid");
		title = request.getParameter("title");
		sourcetype = request.getParameter("sourcetype");
		URL = request.getParameter("URL");
		//templetURL = request.getParameter("templet");
		file = request.getParameter("file");
		firstnodename = request.getParameter("firstnodename");
		internalid = request.getParameter("internalid"); // ??? does not seem to be set
		linkinternalid = request.getParameter("linkinternalid");
		id = request.getParameter("id");
		Name = request.getParameter("Name");
		fx1 = request.getParameter("fx1");
		cnetfile = request.getParameter("cnet");		
		sbA.setLength(0);
		this.usersession = usersession;
		this.out = usersession.out;
		usersession.log.debug("NC:interpreter:out:" + ((out == null) ? "null" : "notnull"));
		this.request = request;
		this.response = responseI;
		
		if (fx != null) {
			usersession.log.debug("NC:fx:" + fx + "  -----------------------");
			if (fx.equalsIgnoreCase("sendGexf")) {
				this.sendGexf();
			} else if (fx.equalsIgnoreCase("sendNeo4j")) {
				this.sendNeo4j();
			} else if (fx.equalsIgnoreCase("sendActionPopup")) {
				this.sendActionPopup();
			} else {
				// transfer control to, invoke(), the method named in fx
				classA = this.getClass();
				try {
					usersession.log.debug("NC:interperter:fx:" + fx);
					methodA = classA.getMethod(fx, new Class[] {});
					methodA.invoke(this, new Class[] {});
				} catch (InvocationTargetException ite) {
					usersession.log.error("NC:interperter:ite:" + usersession.stack2string(ite) + " fx:\"" + fx + "\"");
					throw new ServiceException(this.usersession, request, "NavCon:interperter:ite:" + ite );							
				} catch (IllegalAccessException iae) {
					usersession.log.error("Go:NavCon:interperter:iae:" + usersession.stack2string(iae));
					throw new ServiceException(this.usersession, request,
							"NavCon:interpreter:iae:" + iae + ";fx:" + fx + ";USERID:" + usersession.getUserName());
				} catch (NoSuchMethodException nsme) {
					usersession.log.error("Go:NavCon:interperter:nsme:" + usersession.stack2string(nsme));
					throw new ServiceException(this.usersession, request, "NavCon:interpreter:nsme:" + nsme);
				}
			}
		} else {
			usersession.addtomessage("NavCon a function (fx=) was not specified.");
		}
	} // interpreter

	/**
	 * Requested from the ActionPopUp screen.  internalid is a param which is parsed into a longinternalid
	 * and a new nettree is created by reading through the network
	 */
	public void changeFocus() throws ServiceException {
		try {
			focus = request.getParameter("focus");
			mapname = request.getParameter("mapname");
			usersession.log.debug("NavCon:changeFocus:top.");
			neographworktx = neographdbworking.beginTx();
			usersession.log.debug("NavCon:changeFocus:after begintx, before new Properties.");
			Properties ntgraphprops = new Properties();
			ntgraphprops.put("dummy", "dummy");
			Long longinternal = (Long.parseLong(internalid));
			usersession.log.debug(
					"NavCon:changeFocus:before new Nettree:internalid String:" + internalid + " long:" + longinternal);
			this.nettreeA = new NetTree(this.neographdbworking, neographdbworking.getNodeById(longinternal),
					ntgraphprops, this.usersession);
			usersession.log.debug("NavCon:changeFocus:after new nettree.");
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:changeFocus:" + nfe.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		position.put("focus", focus);
		status = "modified";
		messenger("NC:SGPA", "Focus changed to '" + focus + "'");
		sendNormalBegin();
	} // changeFocus()
	
	// todo test for current maxlevel
	public void changeLevel() throws ServiceException {		
		maxlevel = request.getParameter("maxlevel");	
		int newmaxlevel = Integer.parseInt(maxlevel);
		usersession.log.debug("NavCon:changeLevel:top:currmax:" + usersession.maxlevel + " newlev:" + maxlevel);
		if ( newmaxlevel < usersession.maxlevel ) { 
			usersession.maxlevel = newmaxlevel;
			messenger("NC:CL", "Maxlevel changed to '" + maxlevel + "'");
			this.contract2Level();
		} else {
			messenger("NC:CL", "Maxlevel not changed to '" + newmaxlevel + "'");
			this.sendNormalBegin();
		}
	} // changeLevel
	
	// to output all the lines Todo propbably need to expand all before
	public void toggleOutputLines() throws ServiceException {		
		maxlevel = request.getParameter("toggleOutputLines");			
		if ( usersession.outputLines ) {
			usersession.outputLines = false;
		} else {
			usersession.outputLines = true;
		}		
		messenger("NC:TOL", "ToDo: expand? Output all lines now " + usersession.outputLines );
		sendNormalBegin();
	} // toggleOutputLines

	/**
	 * Expand and contract the NetTree 20180825 rbg change to NetTree
	 */
	public void plusminus() throws ServiceException {
		intlineno = java.lang.Integer.valueOf(request.getParameter("lineno"));
		// netlinehold = (NetLine) nettreeA.linesList.get(intlineno);
		netlinehold = nettreeA.getNetLine(intlineno);
		usersession.log.debug("NavCon:plusminus:after get netlinehold:type:" + netlinehold.type);
		usersession.log.debug("NavCon:plusminus:hypersize:" + netlinehold.adjacentlineref.hyperLineV.size());
		usersession.log.debug("NavCon:plusminus:hyposize:" + netlinehold.adjacentlineref.hypoLineV.size());
		usersession.log.debug("NavCon:plusminus:plusminusneither 1,2:" + netlinehold.plusminusneither);
		if (netlinehold.plusminusneither == PLUS) {
			netlinehold.plusminusneither = MINUS;
			branchExpand(netlinehold, nettreeA, out);
			messenger("NC:plusminus", "NetTree  Expanded.");
		} else if (netlinehold.plusminusneither == MINUS) {
			netlinehold.plusminusneither = PLUS;
			branchContract(netlinehold, nettreeA, out);
			messenger("NC:plusminus", "NetTree contracted.");
		} else {
			netlinehold.plusminusneither = PLUS;
			branchContract(netlinehold, nettreeA, out);
			messenger("NC:SGPA", "NetTree contracted. PMN ToDo.");
			usersession.log.error("NavCon:plusminus:neither plus or minus in line. Maybe to take care of focus.");
		}
		// session.setAttribute("currenthypertree", hypertreeA);
		status = "modified";
		usersession.log.debug("NavCon:plusminus:after branching:");		
		sendNormalBegin();
	} // plusminus()


	/**
	 * Method called with fx=newgraph. opengraph and newgraph functionality
	 */
	public void newgraph() throws ServiceException {
		title = request.getParameter("title");
		sourcetype = request.getParameter("sourcetype");
		URL = request.getParameter("URL");
		file = request.getParameter("file");
		focus = request.getParameter("focus");
		firstnodename = request.getParameter("firstnodename");
		//newgraph(title, sourcetype, URL, file, focus, firstnodename);
	}	

	public void sendNodeProps() throws ServiceException {
		String targetxsl = "graph.xsl";		
		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element NodePropElem;
		String propvalue = "";	
		try {
			neographworktx = neographdbworking.beginTx();		
			String target = "nodeprops.xsl";			
			String id = request.getParameter("id");
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "List");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(usersession.doc, rootElem );
			neonodehold = this.neographdbworking.getNodeById(Long.parseLong(id));			
			neopropiter = neonodehold.getPropertyKeys().iterator();
			while ( neopropiter.hasNext() ) {
				try {
					neopropkey = neopropiter.next();
					propvalue = (String) neonodehold.getProperty(neopropkey);
					NodePropElem = doc.createElement("Prop");
					rootElem.appendChild(NodePropElem);
					// usersession.log.debug("NC:SGT:nodekey:" + neopropkey + " prop:" + propvalue);
					NodePropElem.setAttribute("Name", neopropkey);						
					NodePropElem.setAttribute("Value", propvalue);
					NodePropElem.setAttribute("Type", "String");
				} catch (org.neo4j.graphdb.NotFoundException nfe ) {
					usersession.log.debug("NC:SGT:nfe:" + nfe.toString());
				}
			}
			usersession.log.debug("NavCon:sendNodeProps:before transformout.");
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (NumberFormatException nfe ) {
			messenger("NC:sendNodeProps", "NumberFormatException.");
		} catch (Exception ex) {
			usersession.log.error("NC:sendNodeProps:throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendNodeProps:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			usersession.log.debug("NC:SGT:before trans.close(), ending the beginTx.");
			neographworktx.close();
		}
	} // sendNodeProps



	public void sendNodePropAddRequest() throws ServiceException {
		try {
			String nodename = request.getParameter("nodename");
			String internalid = request.getParameter("internalid");
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "nodeprops.xsl";
			globalElem.setAttribute("mode", "Add");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("internalid", internalid);
			globalElem.setAttribute("nodename", nodename);
			addAnyMessage2DOM(usersession.doc, rootElem );	
			usersession.log.debug("NavCon:sendNodePropAdd:before transformout.");
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendNodePropAdd:throwing ex:" + this.usersession.stack2string(ex));
			throw new ServiceException("NC:sendNodePropAdd:throwing ex:" + this.usersession.stack2string(ex));
		}
	} // sendNodePropAdd
	
	/*
	 * 20190326 
	 */
	public void sendLinkProps() throws ServiceException {
		String targetxsl = "linkprops.xsl";		
		String neopropkey;
		Iterator<String> neopropiter = null;	
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Long neostartnodeid;
		Long neoendnodeid;
		Element LinkElem;
		Element LinkPropElem;
		String id;
		Long longid = null;
		String propvalue = "";
		try {
			neographworktx = neographdbworking.beginTx();	
			id = request.getParameter("id");
			if (id != null) {
				longid = java.lang.Long.parseLong(id);
			}
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "linkprops.xsl";
			globalElem.setAttribute("mode", "List");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(usersession.doc, rootElem );
			neorelationshiphold = this.neographdbworking.getRelationshipById(longid);
			//usersession.log.debug("edge:id:" + neorelationshiphold.getId());
			neostartnodeid = neorelationshiphold.getStartNode().getId();
			neoendnodeid = neorelationshiphold.getEndNode().getId();
			//usersession.log.debug("NC:SGT:neostartnodeid:" + neostartnodeid); 
			//usersession.log.debug("NC:SGT:noendnodeid:" + neoendnodeid );	
			neopropiter = neorelationshiphold.getPropertyKeys().iterator();
			while ( neopropiter.hasNext() ) {	
				try {
					neopropkey = neopropiter.next();		
					propvalue = (String) neorelationshiphold.getProperty(neopropkey);
					usersession.log.debug("NC:SGT:edgekey:" + neopropkey + " edgeprop:" + propvalue);						
					LinkPropElem = doc.createElement("LinkProp");
					rootElem.appendChild(LinkPropElem);						
					LinkPropElem.setAttribute("Name", neopropkey);
					LinkPropElem.setAttribute("Value", propvalue);
					LinkPropElem.setAttribute("Type", "String");
				} catch (org.neo4j.graphdb.NotFoundException nfe ) {
					usersession.log.debug("NC:SGT:edgeprops:nfe:" + nfe.toString());
				} catch (java.lang.ClassCastException cce ) {
					usersession.log.debug("NC:SGT:ClassCastException.");
					messenger("NC:SGT", "ClassCastException.");
				}
			} // neopropiter			
			usersession.log.debug("NavCon:sendLinkProps:before transformout.");
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendLinkProps:throwing ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NavCon:sendLinkProps:" + ":ex:" + ex.toString(), ex);		
		} finally {
			neographworktx.success();
			usersession.log.debug("NC:SLP:before trans.close(), ending the beginTx.");
			neographworktx.close();
		}
	} // sendLinkProps

	public void sendLinkPropAddRequest() throws ServiceException {
		try {
			String linkname = request.getParameter("linkname");
			String linkinternalid = request.getParameter("linkinternalid");
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "linkprops.xsl";
			globalElem.setAttribute("mode", "Add");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("linkinternalid", linkinternalid);
			Element LinkElem = doc.createElement("Link");
			neographworktx = neographdbworking.beginTx();
			neorelationshipwork = neographdbworking.getRelationshipById(Long.parseLong(linkinternalid));			
			Element GraphElem = doc.createElement("Graph");
			GraphElem.appendChild(LinkElem);
			LinkElem.setAttribute("Id", Long.toString(neorelationshipwork.getId()));
			LinkElem.setAttribute("LeftNodeName",neorelationshipwork.getStartNode().toString());
			LinkElem.setAttribute("RightNodeName",neorelationshipwork.getEndNode().toString());
			LinkElem.setAttribute("numprops",Integer.toString(neorelationshipwork.getAllProperties().size() ));
			LinkElem.setAttribute("props",neorelationshipwork.getAllProperties().toString());
			addAnyMessage2DOM(usersession.doc, rootElem );
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);  // linkprops.xsl
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addlink:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:addLink:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:addLink:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		// sendNormalBegin(); // 20190603 ??????????
	} // sendLinkPropAddRequest



	public void sendOpenSource() throws ServiceException {
		//this.sendNormalBegin();
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "sourceopen.xsl";
			globalElem.setAttribute("mode", "XMLURL");
			globalElem.setAttribute("target", target);
			// usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendOpenSourceXML:throwing ex:" + this.usersession.stack2string(ex));
			throw new ServiceException("NC:sendOpenSourceXML:throwing ex:" + this.usersession.stack2string(ex));
		}
		this.sendNormalEnd();
	} // sendOpenSource

	/**
	 * This does not seem to do much besides call sendNetTree
	 * @throws ServiceException
	 */
	public void sendNormalBegin() throws ServiceException {
		usersession.log.debug("NC:SNB:TOP. 3333333333333333333333333333333333");
		usersession.log.debug("NC:SNB:TOP.");
		usersession.log.debug("NC:SNB:TOP. 3333333333333333333333333333333333");
		usersession.log.debug("NC:SNB:TOP:settingsnetset:" + usersession.settingsnetset );
		
		if (  usersession.settingsnetset ) {  
			usersession.log.debug("NC:SNB:settingsnetset:true:Net.name:" + usersession.settingsnet.name );
		} else {
			usersession.log.debug("NC:SNB:settingsnetset:false.");
		}		
		try {
			if ( usersession.xmlshow == false ) {				
				usersession.startNewDoc();
				Element rootElem = usersession.doc.createElement("root");
				usersession.doc.appendChild(rootElem);
				Element globalElem = usersession.doc.createElement("global");
				rootElem.appendChild(globalElem);
				globalElem.setAttribute("maxlevel", String.valueOf(usersession.maxlevel));
				if ( nettreeA != null ) { // NOT
					globalElem.setAttribute("Links", String.valueOf(nettreeA.getEdgeIsPresentedSize() ) ); 
					globalElem.setAttribute("Nodes", String.valueOf(nettreeA.getNodeIsPresentedSize() ) ); 
					globalElem.setAttribute("Level", String.valueOf((nettreeA.getLevel()-1) ));		
				}
				globalElem.setAttribute("outputLines", String.valueOf(usersession.outputLines));
				usersession.genpropman.loadTemplate(usersession.genpropman.templetProps);
				usersession.genpropman.props2DOM(usersession.doc, rootElem, ""); 
				usersession.log.debug("NC:SNB:NTC:NTNet:name:" + usersession.nettreecontrol.NTNet.name );
				if ( usersession.settingsnetset == true ) {
					usersession.log.debug("NC:SNB:settingnetset:true:name:" + usersession.settingsnet.name );
				} else {
					usersession.log.debug("NC:SNB:usersession.settingsnetset == false");
				}
				usersession.log.debug("NC:SNB:NTC:add2DOM:controlNet:Netprops:" + usersession.nettreecontrol.NTNet.getNetProps().keySet().toString());
				usersession.nettreecontrol.add2DOM(usersession.doc, rootElem, ""); 
				usersession.log.debug("NC:SNB:after attempted nettreecontrol add2DOM.");
				addAnyMessage2DOM(usersession.doc, rootElem );
				usersession.transformout(usersession.doc, "menutop.xsl", response);
				/* the guts are here */
				if (usersession.doc != null && response != null) {
					usersession.log.debug("NC:sendNormalBegin:before transformout:target:" + "sendnormal.xsl");
					usersession.transformout(usersession.doc, "sendnormal.xsl", response);
				} else {
					if (usersession.doc != null) {
						usersession.log.debug("NC:SNB:usersession.doc != null || response == null");
					} else if (response != null) {
						usersession.log.debug("NC:SNB:response == null");
					}
				}
			} // endif xmlshow
			//usersession.log.debug("NC:sendNormalBegin:before sendNetTree. xmlshow:" + usersession.xmlshow);
			usersession.log.debug("NC:SNB:fx:" + fx);
			if (this.unettree == null ) {
				usersession.log.debug("NC:SNB:unettree == null.");
			} else {
				usersession.log.debug("NC:SNB:unettree != null. NOT NOT");
			}
			if ( fx.equalsIgnoreCase("sendUN") ) {
				usersession.log.debug("NC:SNB:fx=sendUN;Calling sendUNetTree." );
				sendUNetTree();
			} else {
				usersession.log.debug("NC:SNB:fx=NOTsendUN;Calling sendNetTree." );
				sendNetTree();				
			}
			//usersession.log.debug("NC:sendNormalBegin:after sendNetTree.");
		} catch (Exception ex) {
			usersession.log.error("NC:SNB:throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendNormalBegin:" + ":ex:" + ex.toString(), ex);
		}
		usersession.message4dom.messagewords.clear();
		usersession.log.debug("NC:SNB:BOTTOM. messagewords.clear()ed");
	} // sendNormalBegin
	
	
	/**
	 * 
	 */
	public void sendTraversal() throws ServiceException {
		try {
			TraversalExample example ;
			//neographworktx = neographdbworking.beginTx();
			if (nettreeA != null) {
				usersession.log.debug("NC:ST:nettree != null");			
				friendsTraversal = neographdbworking.traversalDescription().depthFirst().relationships(Rels.KNOWS)
						.uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);
				example = new TraversalExample(neographdbworking, usersession);
				org.neo4j.graphdb.Node dat = example.createData();
				example.run(dat);
			} else {
				messenger("NC:ST", "NetTree == null.");
				usersession.log.debug("NC:ST:NetTree == null.");
			}
			usersession.log.debug("NC:ST:before transout map.xsl");
		} catch (Exception ex) {
			usersession.log.error("NC:Sl:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:SL:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();		
		}
		usersession.log.debug("NC:ST::bottom.");
		sendNormalBegin();
	} // sendTraversal
	
	public void sendResults() throws ServiceException {
		// String rows = "";
		try {
			Map<String, Object > row;		
			usersession.startNewDoc();
			String stat = request.getParameter("stat");
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "showlines.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			neographworktx = neographdbworking.beginTx();
			
			org.neo4j.graphdb.Result result = neographdbworking.execute( stat );
			usersession.log.debug("columns:" + result.columns());
			List<String> columns  = result.columns();
			Iterator columnIter = columns.iterator();
			while ( columnIter.hasNext() ) {
				String columnName = (String) columnIter.next();			
				usersession.log.debug("columnName:" + columnName );
				Iterator<Node> nodevalue = result.columnAs(columnName);
			    while (nodevalue.hasNext()) {								
					Node n = (Node)nodevalue.next();/*
					usersession.log.debug("NC:SL:NodeId:" + n.getId());
					usersession.log.debug("NC:SL:keys:" + n.getPropertyKeys());
					for (String key : n.getPropertyKeys()) {
						usersession.log.debug("NC:SL:" + " key: " + key + " : " + n.getProperty(key) );
					}
					*/
			    }
			}
			usersession.log.debug("NC:SL:before transout map.xsl");
			//usersession.transformout(usersession.doc, "showresults.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:SL:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:SR:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();		
		}
		usersession.log.debug("NC:SR:bottom.");
		sendNormalBegin(); // this goes away
	}  // sendResults

	/**
	 * sends the nettree using sendlines.xsl to format a table
	 */
	public void sendLines() throws ServiceException {
		try {
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);
			
			usersession.genpropman.props2DOM(usersession.doc, rootElem, ""); 
			usersession.transformout(usersession.doc, "menutop.xsl", response); 
			
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "showlines.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			neographworktx = neographdbworking.beginTx();
			if (nettreeA != null) {
				usersession.log.debug("NC:SL:nettree != null");
				nettreeA.add2DOM(usersession.doc, rootElem, "");				
			} else {
				messenger("NC:SNT", "NetTree == null.");
				usersession.log.debug("NC:SL:NetTree == null.");
			}
			usersession.log.debug("NC:SL:before transout map.xsl");
			usersession.transformout(usersession.doc, "showlines.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:Sl:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:SL:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();		
		}
		usersession.log.debug("NC:SNTree::bottom.");
	} // sendLines
	
	/**
	 * sends the nettree using sendlines.xsl to format a table
	 */
	public void sendLineControlInfo() throws ServiceException {
		try {
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);			
			usersession.genpropman.props2DOM(usersession.doc, rootElem, ""); 
			usersession.transformout(usersession.doc, "menutop.xsl", response); 
			
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "linecontrolinfo.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			neographworktx = neographdbworking.beginTx();
			Iterator linedisplaymapsetiter = usersession.linedisplaysys.linetreemaphash.keySet().iterator();
			String linedisplaymapsetiterkey = "";
			while ( linedisplaymapsetiter.hasNext()  ) {
				linedisplaymapsetiterkey = (String) linedisplaymapsetiter.next();
				usersession.log.debug("linedisplaymapsetiter key:" + linedisplaymapsetiterkey );
			}
			for (HashMap.Entry<String, LineSpecSet> entry : usersession.linedisplaysys.linetreemaphash.entrySet()) {
				LineSpecSet linetreemapsetA = entry.getValue();	
				usersession.log.debug("linedispl:" + linetreemapsetA.toString());
			}		
			usersession.log.debug("NC:SendLineControlInfo:before transout map.xsl");
			usersession.transformout(usersession.doc, "linecontrolinfo.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:SendLineControlInfo:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:SL:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();		
		}
		usersession.log.debug("NC:SendLineControlInfo:bottom.");
	} // sendLineControlInfo
	
	/**
	 * sends the nettree using sendNets.xsl to format complete page
	 * @throws ServiceException
	 */
	public void sendNets() throws ServiceException {
		usersession.log.debug("NC:sendNets:top.");
		usersession.message4dom.messagewords.add("sendNets:Testing message.");
		if (!usersession.showView) { // NOT 
			return;
		}
		try {
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);			
			usersession.genpropman.props2DOM(usersession.doc, rootElem, "");
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "sendNets.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			if (usersession.message4dom.messagewords.size() > 0) {
				this.usersession.message4dom.add2DOM(usersession.doc, globalElem, "");
			}
			neographworktx = neographdbworking.beginTx();
			if (nettreeA != null) {
				usersession.log.debug("NC:sendNets:nettree != null");
				nettreeA.add2DOM(usersession.doc, rootElem, "");				
			} else {
				messenger("NC:sendNets", "NetTree == null.");
				usersession.log.debug("NC:sendNets:NetTree == null.");
			}
			usersession.log.debug("NC:sendNets:before transout sendNets.xsl");
			usersession.transformout(usersession.doc, "sendNets.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:sendNets:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:sendNets:ex:" + ex.toString(), ex);
		} finally {
			usersession.message4dom.messagewords.clear();
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:sendNets:bottom.");
	} // sendNets	

	/**
	 * sends the UNetTree using map.xsl to format
	 * 
	 * 20210903 WIP goal to have n.n.Net operational
	 */
	public void sendUNetTree() throws ServiceException {
		usersession.log.debug("NC:SUNTree:top.");
		if (!usersession.showView) { // NOT 
			return;
		}
		try {
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);			
			usersession.genpropman.props2DOM(usersession.doc, rootElem, "");
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "map.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			usersession.log.debug("NavCon:version:" + this.version + " before nettree.add2DOM.");
			if (unettree != null) {
				usersession.log.debug("NC:SUNT:unettree != null");
				unettree.add2DOM(usersession.doc, rootElem, "");				
			} else {
				messenger("NC:SUNT", "unettree == null.");
				usersession.log.debug("NC:SUNT:NetTree == null.");
			}
			usersession.log.debug("NC:SUNT:before transout map.xsl");
			usersession.transformout(usersession.doc, "map.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:SUNT:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:sendUNetTree:ex:" + ex.toString(), ex);
		}
		usersession.log.debug("NC:SUNTree::bottom.");
	} // sendUNetTree	
	
	/**
	 * sends the nettree using map.xsl to format
	 * @throws ServiceException
	 */
	private void sendNetTree() throws ServiceException {
		usersession.log.debug("NC:SNT:top.");
		if (!usersession.showView) { // NOT 
			return;
		}
		try {
			// Document doc = usersession.startNewDoc();
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);			
			usersession.genpropman.props2DOM(usersession.doc, rootElem, "");
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", "map.xsl");
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("visableMap", this.mapname);
			neographworktx = neographdbworking.beginTx();
			if (nettreeA != null) {
				usersession.log.debug("NC:SNT:nettree != null");
				nettreeA.add2DOM(usersession.doc, rootElem, "");				
			} else {
				messenger("NC:SNT", "NetTree == null.");
				usersession.log.debug("NC:SNT:NetTree == null.");
			}
			usersession.log.debug("NC:SNT:before transout map.xsl");
			usersession.transformout(usersession.doc, "map.xsl", response); 
		} catch (Exception ex) {
			usersession.log.error("NC:SNT:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:sendNetTree:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:SNTree::bottom.");
	} // sendNetTree

	/**
	 * added trap for xmlshow
	 * 
	 * @throws ServiceException
	 * ??? think this should be dead / but used in send props...
	 */
	private void sendNormalEnd() throws ServiceException {
		usersession.log.debug("NC:sendNormalEnd:top.");
		if (!usersession.xmlshow) {
			try {
				Document doc = usersession.startNewDoc();
				Element globalElem = doc.createElement("global");
				doc.appendChild(globalElem);
				String target = "sendnormal.xsl";
				globalElem.setAttribute("mode", "End");
				globalElem.setAttribute("NodeKnownAs", usersession.genpropman.getProperty("NodeKnownAs").getValue());
				globalElem.setAttribute("LinkKnownAs", usersession.genpropman.getProperty("LinkKnownAs").getValue());
				globalElem.setAttribute("GraphKnownAs", usersession.genpropman.getProperty("GraphKnownAs").getValue());
				usersession.transformout(doc, target, response);
			} catch (Exception ex) {
				usersession.log.error("NC:sendLinkProps:throwing ex:" + usersession.stack2string(ex));
				throw new ServiceException(this.usersession, request, "NavCon:sendNormalEnd:" + ":ex:" + ex.toString(),
						ex);
			}
		}
		usersession.message4dom.messagewords.clear(); // rbg ??? 20190329
		usersession.log.debug("NC:sendNormalEnd:bottom. messagewords.clear()ed.");
	} // sendNormalEnd



	/**
	 * sendHelp
	 */
	public void sendHelp() throws ServiceException {
		try {
			// this.sendNormalBegin();
			usersession.startNewDoc();
			Element globalElem = usersession.doc.createElement("global");
			usersession.doc.appendChild(globalElem);
			usersession.transformout(usersession.doc, "menutop.xsl", response);
			usersession.transformout(usersession.doc, "help.xsl", response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendHelp():throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendHelp:" + ":ex:" + ex.toString(), ex);
		}
	} // sendHelp

	/**
	 * sendMenu
	 */
	public void sendMenu() throws ServiceException {
		try {	
			this.sendNormalBegin();						
			usersession.startNewDoc();
			Element globalElem = usersession.doc.createElement("global");
			usersession.doc.appendChild(globalElem);
			if (usersession.message4dom.messagewords.size() > 0) {
				this.usersession.message4dom.add2DOM(usersession.doc, globalElem, "");
			}
			usersession.transformout(usersession.doc, "menu.xsl", response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendMenu():throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendMenu:" + ":ex:" + ex.toString(), ex);
		}
	} // sendMenu
	
	/**
	 * send screen to manage properties
	 */
	public void sendManageProperties() throws ServiceException {
		try {		
			Document doc = usersession.startNewDoc();
			usersession.log.debug("NC:sendManageProperties():usersession.doc:" + usersession.doc.toString() );
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.log.debug("NC:sendManageProperties():before loadProps:doc:" + doc.toString() );
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.log.debug("NC:sendProperties():after loadProps:doc:" + doc.toString() );
			usersession.transformout(doc, "menutop.xsl", response);								
			usersession.transformout(doc, "manageproperties.xsl", response);
		} catch (Exception ex) {
			messenger("NC:SP", "An Error occured, consult map.log. ");
			usersession.log.error("NC:sendManageProperties():throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendManageProperties:" + ":ex:" + ex.toString(), ex);
		}
	} // sendProperties	
	
	/**
	 * send properties that are currently in use
	 */
	public void sendProperties() throws ServiceException {
		try {		
			Document doc = usersession.startNewDoc();
			usersession.log.debug("NC:sendProperties():usersession.doc:" + usersession.doc.toString() );
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.log.debug("NC:sendProperties():before loadProps:doc:" + doc.toString() );
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.log.debug("NC:sendProperties():after loadProps:doc:" + doc.toString() );
			usersession.transformout(doc, "menutop.xsl", response);								
			usersession.transformout(doc, "properties.xsl", response);
		} catch (Exception ex) {
			messenger("NC:SP", "An Error occured, consult map.log. ");
			usersession.log.error("NC:sendProperties():throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendProperties:" + ":ex:" + ex.toString(), ex);
		}
	} // sendProperties
	
	/*
	public void selectAreaTemplet() throws ServiceException {
			//templetURL = request.getParameter("URL");
			usersession.log.debug("NC:SAT:URL:" + templetURL);
			usersession.genpropman.openConnectionLoadTempletProps(templetURL);
			resetLineDisplaySys();
			//sendProperties();
	} // selectAreaTemplet
	*/
	
	/*
	 * For testing 20190329
	 */
	public void clearMessageWords() throws ServiceException {
		usersession.message4dom.messagewords.clear();
		this.sendNormalBegin();
	}
	
	public void sendStats() throws ServiceException {
		messenger("NC:SGexf:", "Links " + nettreeA.getEdgeIsPresentedSize() 
			+ ", Nodes "+ nettreeA.getNodeIsPresentedSize() 
			+ ", Level " + (nettreeA.getLevel()-1)
			+ ", MaxLevel " + usersession.maxlevel 
			+ ", LogLevel " + usersession.log.getLevel()
			+ ", Control " + usersession.nettreecontrol.NTNet.name
			);
		this.sendNormalBegin();
	}
	
	/**
	 * Write current graph as a dot file to screen
	 * 
	 * @throws ServiceException
	 */
	public void sendWriteDot() throws ServiceException {
		usersession.log.debug("NC:sendWriteDot():top.");
		// file as name gotten in request &file=xxyzzy
		this.writeDot(neographdbworking, file);
		usersession.log.debug("NC:sendWriteDot:bottom.");
	} // sendWriteDot()
	
	/**
	 * Write current neograph as a gexf file to be displayed 
	 * 
	 * @throws ServiceException
	 */
	public void sendWriteGexf() throws ServiceException {
		usersession.log.debug("NC:sendWriteGexf():top.");
		this.writeGexf(neographdbworking);
		usersession.log.debug("NC:sendWriteGexf:bottom.");
	} // sendWriteGexf()

	public void executeBuilder() throws ServiceException {
		try {
			usersession.log.debug("<br>Top:ver" + version + "<br>");
			FileWriter fstream = null;
			String fileName = "/var/www/html/map/eb.txt";
			usersession.log.debug("NavCon:EB:before new file writer");
			fstream = new FileWriter(fileName);
			ArrayList<String> alist = new ArrayList<String>();
			alist.clear();
			alist.add("/bin/sh");
			alist.add("-c");
			alist.add("./drawngraph.sh");

			usersession.log.debug("<br>Before execution:ver" + version + "<br>");
			ProcessBuilder processBuilderA = new ProcessBuilder(alist);

			final Process processB = processBuilderA.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(processB.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(processB.getErrorStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null) {
				usersession.log.debug("stdI:" + s);
				fstream.write(s);
			}
			while ((s = stdError.readLine()) != null) {
				usersession.log.debug("stdE:" + s);
			}
			fstream.close();
			response.sendRedirect("http://localhost/map/garveytree.jpg");
			this.sendNormalBegin();
		} catch (IOException ioe) {
			usersession.log.error("NC:ExecuteBuilder:ioe:" + ioe.toString());
			usersession.addtomessage("ExecuteBuilder:Problem");
		}
	} // executeBuilder

	public void sendLinkList() throws ServiceException {
		usersession.log.debug("NC:sendLinkList:top.");
		int linkcount = 0;
		Element LinkElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Long neostartnodeid;
		Long neoendnodeid;
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.genpropman.props2DOM(doc, rootElem, ""); 
			usersession.transformout(doc, "menutop.xsl", response); 
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "links.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			try {
				usersession.log.debug("NC:sendLinkList:top.");
				neographworktx = neographdbworking.beginTx();
				ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking
						.getAllRelationships().iterator();
				while (neorelationshipiter.hasNext()) {
					linkcount++;

					neorelationshiphold = neorelationshipiter.next();
					usersession.log.debug("edge:id:" + neorelationshiphold.getId());
					neostartnodeid = neorelationshiphold.getStartNode().getId();
					neoendnodeid = neorelationshiphold.getEndNode().getId();
					usersession.log.debug("NC:SGT:neostartnodeid:" + neostartnodeid);
					usersession.log.debug("NC:SGT:noendnodeid:" + neoendnodeid);
					LinkElem = doc.createElement("Link");
					rootElem.appendChild(LinkElem);
					LinkElem.setAttribute("Id", Long.toString(neorelationshiphold.getId()));
					LinkElem.setAttribute("LeftNodeName", neostartnodeid.toString());
					LinkElem.setAttribute("RightNodeName", neoendnodeid.toString());
					LinkElem.setAttribute("numprops", Integer.toString(neorelationshiphold.getAllProperties().size()));
					LinkElem.setAttribute("props", neorelationshiphold.getAllProperties().toString());
				}
			} catch (NumberFormatException nfe) {
				throw new ServiceException("NC:sendLinkList:" + nfe.toString());
			} catch (Exception e) {
				usersession.log.error("NavCon:addNodeProp:e:" + usersession.stack2string(e));
				throw new ServiceException("NavCon:addNodeProp:e:" + e.toString());
			} finally {
				neographworktx.success();
				neographworktx.close();
			}
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.error("NC:sendLinkList:throwing ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NavCon:sendLinkList:" + ":ex:" + ex.toString(), ex);
		}
		usersession.log.debug("NC:sendLinkList:bottom.");
	} // sendLinkList

	public void addnode() throws ServiceException {
		String nodename = request.getParameter("nodename"); 
		String idname = request.getParameter("idname");
		String label = request.getParameter("label");		
		usersession.log.debug("NavCon:addnode:nodename:" + nodename);
		try {
			usersession.log.debug("NavCon:addnode:top.");
			neographworktx = neographdbworking.beginTx();
			Properties ntgraphprops = new Properties();
			ntgraphprops.put("dummy", "dummy");
			if ( !nodename.isEmpty()) {  // NOT NOT NOT
				neonodework = neographdbworking.createNode();
				neonodework.setProperty("Name", nodename);
				Long longinternal = neonodework.getId();
				String intidStr = String.valueOf(longinternal);
				neonodework.setProperty("internalid", intidStr);
				usersession.log.debug("NavCon:addnode:internalid:"  + intidStr);
				this.nettreeA = new NetTree(this.neographdbworking, neographdbworking.getNodeById(longinternal),
						ntgraphprops, this.usersession);				
				usersession.log.debug("NavCon:addnode:after new nettree.");
				messenger("NC:AN", "Node Added:" + nodename);
			} else {
				messenger("NC:AN:", "Node name must not be empty.");
			}

		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addnode:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:addNode:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:addNode:e:" + e.toString());
		} finally {			
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:addnode:after close, before sendNormal.");
		
		sendNodeAddRequest();
		usersession.message4dom.messagewords.clear(); // rbg ??? 20190602
		//sendNormalBegin();
	} // addnode

	public void addNodeProp() throws ServiceException {
		String nodename = request.getParameter("nodename").trim();
		String propname = request.getParameter("propname").trim();
		String unlisted = request.getParameter("unlisted").trim();
		String propvalue = request.getParameter("propvalue").trim();
		String internalid = request.getParameter("internalid").trim();
		try {		
			usersession.log.debug("NC:addNodeProp:top.");
			neographworktx = neographdbworking.beginTx();			
			usersession.log.debug("NC:addNodeProp:values:" + "nn" + nodename 
					+ " pn:" + propname + " ul:" + unlisted + " pv:" + propvalue + " ii:" + internalid  );
			if (propname.equals("")) {
				usersession.log.debug("NC:addNodeProp:propname blank.");
				messenger("NC:ANP", "Property Name must not be blank.");
				sendNormalBegin();
			}
			if (propname.equalsIgnoreCase("Unlisted") ) {
				usersession.log.debug("NC:addNodeProp:unlisted.");
				propname = unlisted;
			}
			neonodework = neographdbworking.getNodeById(Long.parseLong(internalid));
			if (neonodework != null ) {
				usersession.log.debug("NC:addNodeProp:neonodework != null." + " ii:" + neonodework.getId());				 								
				usersession.log.debug("NC:addNodeProp:neonodework props:" + neonodework.getPropertyKeys());
				neonodework.setProperty(propname, propvalue);
				messenger("NC:ANP", "Property:" + propname + " Propovalue:" + propvalue + " Added to Node " + nodename + ".");				
				sendNormalBegin();
			} else {
				usersession.log.debug("NC:addNodeProp:neonodework == null.");
			}
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addNodeProp:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:addNodeProp:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:addNodeProp:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
	} // addNodeProp

	
	/**
	 * ??? unknown status 20190525
	 */
	public void testCypher() throws ServiceException {				
		String linkname = request.getParameter("linkname");
		String initnodename = request.getParameter("initnodename");
		String termnodename = request.getParameter("termnodename");
		String id = request.getParameter("id");
		String key = "";
		usersession.log.debug("NavCon:testCypher");
		org.neo4j.graphdb.Result cypherR = null;
		Map<String, Object> cypherMap = new HashMap<>(); 
		try {		
			neographworktx = neographdbworking.beginTx();
			cypherR = neographdbworking.execute("MATCH (n)-->() RETURN n;");
			while ( cypherR.hasNext() ) {
				cypherMap = cypherR.next();
				usersession.log.debug("NC:testcypher:cypherMap:" + cypherMap);		
			}	
			usersession.log.debug("NC:before keys.");
			keysetiter =  neographdbworking.getAllPropertyKeys().iterator();
			while ( keysetiter.hasNext() ) {
				key = keysetiter.next();
				usersession.log.debug("NC:testcypher:key:" + key);
			}
			usersession.log.debug("NC:before keys.");
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addlink:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:testcypher:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:testcypher:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:testcypher:after close, before sendNormal.");
		sendNormalBegin();
	}	
	
	/**
	 * addlink request with linkname, left and right node names
	 */
	public void addlink() throws ServiceException {
		String linkname = request.getParameter("linkname");
		String leftnodename = request.getParameter("leftnodename"); // actually left node id
		String rightnodename = request.getParameter("rightnodename"); // actually right node id
		usersession.log.debug("NavCon:addlink:left:" + leftnodename + " right:" + rightnodename);
		try {
			neographworktx = neographdbworking.beginTx();
			Properties ntgraphprops = new Properties(); // ???? this is used in new NetTree but why
			ntgraphprops.put("dummy", "dummy");
			neonodeworkLeft = usersession.getNodeByName(leftnodename);
			if ( neonodeworkLeft == null  ) {
				neonodeworkLeft = neographdbworking.createNode();
				neonodeworkLeft.setProperty("Name", leftnodename);
			} else {
				messenger("NC:addLink", "left node allready exists.");
			}
			neonodeworkRight = usersession.getNodeByName(rightnodename);
			if ( neonodeworkRight == null  ) {
				neonodeworkRight = neographdbworking.createNode();
				neonodeworkRight.setProperty("Name", rightnodename);
			} else {
				messenger("NC:addLink", "right node allready exists.");
			}
			neorelationshipwork = usersession.getLinkByName(linkname); 
			if (  neorelationshipwork == null )  {						
				neorelationshipwork = neonodeworkLeft.createRelationshipTo(neonodeworkRight, MyRels.LINK);
				usersession.log.debug("NavCon:addlink:RelId:" + neorelationshipwork.getId() + " leftinternal:"
					+ neonodeworkLeft.getId() + " right:" + neonodeworkLeft.getId());
				neorelationshipwork.setProperty("Name", linkname);
			} else {
				messenger("NC:addLink", "link as named allready exists.");
			}
			
			neorelationshipwork.setProperty("linkinternalid", neorelationshipwork.getId());
			neorelationshipwork.setProperty("Relationship", "LINK");
			// ????????? what is this ?? looks like it sets the focus for the nettree
			Long longinternal = neonodeworkLeft.getId();
			this.nettreeA = new NetTree(this.neographdbworking, neographdbworking.getNodeById(longinternal), 
					ntgraphprops, this.usersession);
			messenger("NC:AL", "Link Added:" + linkname);
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addlink:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:addLink:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:addLink:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:addlink:after close, before sendNormal.");
		sendNormalBegin();
	} // addlink()	
		

	public void addLinkProp() throws ServiceException {
		String linkname = request.getParameter("linkname").trim();
		String propname = request.getParameter("propname").trim();
		String unlisted = request.getParameter("unlisted").trim();
		String propvalue = request.getParameter("propvalue").trim();
		String linkinternalid = request.getParameter("linkinternalid").trim();
		usersession.log.debug("NavCon:addlinkProp:LI:" + linkinternalid + " PV:" + propvalue);
		try {
			neographworktx = neographdbworking.beginTx();
			neorelationshipwork = neographdbworking.getRelationshipById(Long.parseLong(linkinternalid));
			neorelationshipwork.setProperty(propname, propvalue);
			usersession.log.debug("NavCon:addlinkProp:propset." );
			Properties ntgraphprops = new Properties();
			ntgraphprops.put("dummy", "dummy");
			if (propname.equalsIgnoreCase("Unlisted") || propname.equals("")) {
				propname = unlisted;
			}
			if (neorelationshipwork != null) {
				messenger("NC:ALP", "link with name " + linkname + " does not exist.");
				sendNormalBegin();
			} else if (linkname != null) {
				messenger("NC:ALP", "Property " + propname + " Added to Link " + linkname + ".");
				sendNormalBegin();
			}
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:addlinkProp:" + nfe.toString());
		} catch (org.neo4j.graphdb.NotFoundException nnf) {
			usersession.log.error("NavCon:addLinkPropo:nfe:" + nnf.toString());
			messenger("NC:ALP", nnf.toString() );
		} catch (Exception e) {
			usersession.log.error("NavCon:addLinkProp:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:addLinkProp:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:addlink:after close, before sendNormal.");
		//sendNormalBegin();
	} // addLinkProp

	public void sendLinkAUDI() throws ServiceException {
		ArrayList<GenProp> displaynodeProps = new ArrayList<>();
		ArrayList<GenProp> linkrefProps = new ArrayList<>();
		try {
			neographworktx = neographdbworking.beginTx();
			Document doc = usersession.startNewDoc();			
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps);
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.transformout(doc, "menutop.xsl", response);
			rootElem.appendChild(globalElem);
			String target = "audiLinkpop.xsl";
			//globalElem.setAttribute("mode", "normal");
			//globalElem.setAttribute("target", target);
			//globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			
			try {
				intlineno = java.lang.Integer.valueOf(lineno); // lineno from request.getParam
				usersession.log.debug("NavCon:SendActionPop:before getNetLine:intlineno:" + intlineno);
				netlinehold = nettreeA.getNetLine(intlineno); // ???? todo should change to direct use of node and link
				usersession.log.debug("NavCon:SendActionPop:before setAttribute:");
				if ( netlinehold.linkref.getStartNode().getProperty("Name").toString() != null ) {	 // 20190725
					globalElem.setAttribute("initNodeName", netlinehold.linkref.getStartNode().getProperty("Name").toString());
					globalElem.setAttribute("rightnodeame", netlinehold.linkref.getStartNode().getProperty("Name").toString());
				} else if ( netlinehold.linkref.getStartNode().getProperty("Label").toString() != null ) {
					globalElem.setAttribute("initNodeName", netlinehold.linkref.getStartNode().getProperty("Label").toString());
				} else {
					messenger("NC:SLA", "No left name.");
				}
				if ( netlinehold.linkref.getEndNode().getProperty("Name").toString() != null ) {
					globalElem.setAttribute("termNodeName", netlinehold.linkref.getEndNode().getProperty("Name").toString());
					globalElem.setAttribute("leftnodename", netlinehold.linkref.getEndNode().getProperty("Name").toString());
				} else if ( netlinehold.linkref.getEndNode().getProperty("Label").toString() != null ) {
					globalElem.setAttribute("initNodeName", netlinehold.linkref.getEndNode().getProperty("Label").toString());
				} else {
					messenger("NC:SLA", "No right name.");
				}
				if ( netlinehold.linkref.getProperty("Name") != null ) {
					globalElem.setAttribute("linkname", String.valueOf(netlinehold.linkref.getProperty("Name")));
				} else {
					globalElem.setAttribute("linkname", "Linkname unknown.");
					messenger("NC:SLA", "No link name.");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.error("NavCon:sendActionPopup:nnf:" + nnf.toString());
				messenger("NC:SLA", nnf.toString() );
			} catch (NullPointerException npe) {
				usersession.log.debug("NavCon:actionpopup:NullPointer.");
				messenger("NC:SLA:null pointer", "SLA:Error null pointer." );
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				usersession.log.debug("NavCon:ArrayIndexOutOfBoundsException.");
				messenger("NC:SLA:aioobe", "Error aioobe" );
			} finally {
				usersession.log.debug("NavCon:SendActionPop:before add2line DOM");
				netlinehold.add2DOM(doc, globalElem, "line");
			}
			
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.debug("NC:SLA:e:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NC:SLA:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
	} // sendLinkAUDI
	
	/**
	 * Puts out a popup type window as a menu for what to do with the selected line
	 * this uses the neographdb. 20180818 1314 rbg initial copied from action popup
	 * ToDo copy actionpop.xsl to linepop.xsl and update ToDo change to direct use
	 * of node and link vs. reading through lines
	 * 20190513 messing with properties genpropmanager types
	 */
	public void sendActionPopup() throws ServiceException {
		ArrayList<GenProp> displaynodeProps = new ArrayList<>();
		ArrayList<GenProp> linkrefProps = new ArrayList<>();
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.transformout(doc, "menutop.xsl", response);
			rootElem.appendChild(globalElem);
			String target = "actionpop.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			/*
			if (usersession.settingsoroldcontrol.equals("settings") ) {
				try {
					intlineno = java.lang.Integer.valueOf(lineno); // lineno from request.getParam
					usersession.log.debug("NavCon:SendActionPop:before getNetLine:intlineno:" + intlineno);
					netlinehold = this.unettree.getNetLine(intlineno);
					netlinehold = nettreeA.getNetLine(intlineno); 
					usersession.log.debug("NavCon:SendActionPop:before setAttribute:");
					if (netlinehold.linkref.leftnode.getNprop("Name").toString() != null) { // 20190725
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.leftnode.getNprop("Name").toString());
						globalElem.setAttribute("rightnodename",
								netlinehold.linkref.rightnode.getNprop("Name").toString());
					} else if (netlinehold.linkref.leftnode.getNprop("Label").toString() != null) {
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.leftnode.getNprop("Label").value);
					} else {
						messenger("NC:SAP", "No left name.");
					}
					if (netlinehold.linkref.getEndNode().getProperty("Name").toString() != null) {
						globalElem.setAttribute("termNodeName",
								netlinehold.linkref.getEndNode().getProperty("Name").toString());
						globalElem.setAttribute("leftnodename",
								netlinehold.linkref.getEndNode().getProperty("Name").toString());
					} else if (netlinehold.linkref.getEndNode().getProperty("Label").toString() != null) {
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.getEndNode().getProperty("Label").toString());
					} else {
						messenger("NC:SAP", "No right name.");
					}
					if (netlinehold.linkref.getProperty("Name") != null) {
						globalElem.setAttribute("linkname", String.valueOf(netlinehold.linkref.getProperty("Name")));
					} else {
						globalElem.setAttribute("linkname", "Linkname unknown.");
						messenger("NC:SAP", "No link name.");
					}
				} catch (org.neo4j.graphdb.NotFoundException nnf) {
					usersession.log.error("NavCon:sendActionPopup:nnf:" + nnf.toString());
					messenger("NC:SAP", nnf.toString());
				} catch (NullPointerException npe) {
					usersession.log.debug("NavCon:actionpopup:NullPointer.");
					messenger("NC:SAP:null pointer", "SAP:Error null pointer.");
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					usersession.log.debug("NavCon:ArrayIndexOutOfBoundsException.");
					messenger("NC:SAP:aioobe", "Error aioobe");
				} finally {
					usersession.log.debug("NavCon:SendActionPop:before add2line DOM");
					netlinehold.add2DOM(doc, globalElem, "line");
				}
				usersession.transformout(doc, target, response);
			} else {
			*/
				
				try {
					neographworktx = neographdbworking.beginTx();
					intlineno = java.lang.Integer.valueOf(lineno); // lineno from request.getParam
					usersession.log.debug("NavCon:SendActionPop:before getNetLine:intlineno:" + intlineno);
					netlinehold = nettreeA.getNetLine(intlineno); // ???? todo should change to direct use of node and
																	// link
					usersession.log.debug("NavCon:SendActionPop:before setAttribute:");
					if (netlinehold.linkref.getStartNode().getProperty("Name").toString() != null) { // 20190725
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.getStartNode().getProperty("Name").toString());
						globalElem.setAttribute("rightnodename",
								netlinehold.linkref.getStartNode().getProperty("Name").toString());
					} else if (netlinehold.linkref.getStartNode().getProperty("Label").toString() != null) {
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.getStartNode().getProperty("Label").toString());
					} else {
						messenger("NC:SAP", "No left name.");
					}
					if (netlinehold.linkref.getEndNode().getProperty("Name").toString() != null) {
						globalElem.setAttribute("termNodeName",
								netlinehold.linkref.getEndNode().getProperty("Name").toString());
						globalElem.setAttribute("leftnodename",
								netlinehold.linkref.getEndNode().getProperty("Name").toString());
					} else if (netlinehold.linkref.getEndNode().getProperty("Label").toString() != null) {
						globalElem.setAttribute("initNodeName",
								netlinehold.linkref.getEndNode().getProperty("Label").toString());
					} else {
						messenger("NC:SAP", "No right name.");
					}
					if (netlinehold.linkref.getProperty("Name") != null) {
						globalElem.setAttribute("linkname", String.valueOf(netlinehold.linkref.getProperty("Name")));
					} else {
						globalElem.setAttribute("linkname", "Linkname unknown.");
						messenger("NC:SAP", "No link name.");
					}
				} catch (org.neo4j.graphdb.NotFoundException nnf) {
					usersession.log.error("NavCon:sendActionPopup:nnf:" + nnf.toString());
					messenger("NC:SAP", nnf.toString());
				} catch (NullPointerException npe) {
					usersession.log.debug("NavCon:actionpopup:NullPointer.");
					messenger("NC:SAP:null pointer", "SAP:Error null pointer.");
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					usersession.log.debug("NavCon:actionpopup;ArrayIndexOutOfBoundsException.");
					messenger("NC:SAP:aioobe", "Error aioobe");
				} finally {
					usersession.log.debug("NavCon:SendActionPop:before add2line DOM");
					netlinehold.add2DOM(doc, globalElem, "line");
				}
				usersession.transformout(doc, target, response);
				
			// }
		} catch (Exception ex) {
			usersession.log.debug("NavCon:sendLinePopup:e:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:actionpop:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
	} // sendActionPopup

	/*
	 * this may be obsolete also ???? 20190419
	 */
	public void sendWriteGraph() throws ServiceException {
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "writegraph.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(usersession.doc, rootElem );
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			throw new ServiceException(this.usersession, request, "NavCon:sendWriteGraph:" + ":ex:" + ex.toString(),
					ex);
		}
		//this.sendNormalEnd();
	} // sendWriteGraph()
	
	/*
	 * To provide a count of nodes and links, probably wont do any more than tables
	 */
	public void testGraph( ) throws ServiceException {
		usersession.log.debug("NC:testGraph: BEGIN +++++++++++++++++++++++");	
		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		String propvalue = "";		
		int nodecount = 0;
		int linkcount = 0;
		try {
			neographworktx = neographdbworking.beginTx();				
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			while ( neonodeiter.hasNext() ) {
				nodecount++;
				neonodehold = neonodeiter.next();
				usersession.log.debug("NC:TG:neonode:id:" + neonodehold.getId() );				
				neopropiter = neonodehold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {
					try {
						neopropkey = neopropiter.next();
						propvalue = (String) neonodehold.getProperty(neopropkey);
						usersession.log.debug("NC:TG:key/value:" + neopropkey + "/" + propvalue);
					} catch (org.neo4j.graphdb.NotFoundException nfe ) {
						usersession.log.debug("NC:testGraph:nfe:" + nfe.toString());
					}
				}
			}			
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;				
			propvalue = "";
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
			while ( neorelationshipiter.hasNext() ) {
				linkcount++;
				neorelationshiphold = neorelationshipiter.next();
				usersession.log.debug("NC:testGraph:edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:testGraph:neostartnodeid:" + neostartnodeid); 
				usersession.log.debug("NC:testGraph:noendnodeid:" + neoendnodeid );	
				neopropiter = neorelationshiphold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {	
					try {
						neopropkey = neopropiter.next();		
						propvalue = (String) neorelationshiphold.getProperty(neopropkey);
						usersession.log.debug("NC:testGraph:edgekey:" + neopropkey + " edgeprop:" + propvalue);						
					} catch	(ClassCastException cce ) {
						usersession.log.debug("NC:testGraph:Class Cast Exception ");
					} catch (org.neo4j.graphdb.NotFoundException nfe ) {
						usersession.log.debug("NC:testGraph:edgeprop:nfe:" + nfe.toString());
					}
				}
			}
			// sendNormalBegin();
		} catch (Exception e ) {
			usersession.log.error("NC:TG:error:" + e.toString());
		} finally {
			neographworktx.success();
			//usersession.log.debug("NC:TG:before trans.close(), ending the beginTx.");
			neographworktx.close();
		}
		usersession.log.debug("NC:testGraph: END nodecount/linkcount:" + nodecount + "/" + linkcount);	
	}	// testGraph
	
	/*
	 * Uses cytoscape.js 
	 */
	public void sendDrawnGraph( ) throws ServiceException {
		usersession.log.debug("NC:sendDrawnGraph: BEGIN ++++++++++++");		
		String targetxsl = "draw.xsl";		
		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element GraphElem;
		Element NodeElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Element LinkElem;
		Element streamElem;
		String propvalue = "";		
		try {
			Document doc = usersession.startNewDoc();			
			GraphElem = doc.createElement("Graph");
			doc.appendChild(GraphElem);
			neographworktx = neographdbworking.beginTx();			
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			// iterate over neonodes
			while ( neonodeiter.hasNext() ) {
				neonodehold = neonodeiter.next();
				usersession.log.debug("NC:SDG:neonode:id:" + neonodehold.getId() );				
				NodeElem = doc.createElement("Node");
				GraphElem.appendChild(NodeElem);
				NodeElem.setAttribute("id", Long.toString(neonodehold.getId()));
				NodeElem.setAttribute("Name", neonodehold.getProperty("Name").toString());
				/*
				if ( neonodehold.getLabels() != null ) {
					NodeElem.setAttribute("Name", neonodehold.getLabels().toString());
				}
				*/
				NodeElem.setAttribute("numprops", Integer.toString(neonodehold.getAllProperties().size( )));
			}			
			// now the relationships / links
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;				
			propvalue = "";
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
			while ( neorelationshipiter.hasNext() ) {
				neorelationshiphold = neorelationshipiter.next();
				//usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:SDG:neostartnodeid:" + neostartnodeid); 
				usersession.log.debug("NC:SDG:noendnodeid:" + neoendnodeid );	
				LinkElem = doc.createElement("Link");
				GraphElem.appendChild(LinkElem);
				LinkElem.setAttribute("Id", Long.toString(neorelationshiphold.getId()));
				//LinkElem.setAttribute("Name",neorelationshiphold.getProperty("Name").toString());
				//LinkElem.setAttribute("LeftNodeName", neorelationshiphold.getStartNode().getProperty("Name").toString());
				//LinkElem.setAttribute("RightNodeName", neorelationshiphold.getEndNode().getProperty("Name").toString());				
				LinkElem.setAttribute("LeftNodeName", Long.toString(neorelationshiphold.getEndNodeId()));
				LinkElem.setAttribute("RightNodeName", Long.toString(neorelationshiphold.getStartNodeId()));
				//LinkElem.setAttribute("numprops",Integer.toString(neorelationshiphold.getAllProperties().size() ));
			}
			usersession.transformout(doc, targetxsl, response);
		} catch (Exception e ) {
			usersession.log.error(e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:sendDrawnGraph: END  +++++");	
	}	// sendDrawnGraph 
		
	/**
	 * Send formatted file, DOT, JSON
	 * 20200101 1400 rbg orig
	 */
	public void sendFormattedFile() throws ServiceException {
		usersession.log.debug("NC:sendFormatteds: BEGIN ++++++++++++");
		fileformat = request.getParameter("fileformat");
		String sendtofile = " ";
		sendtofile = request.getParameter("sendtofile");
		if (sendtofile == null || sendtofile.trim().equals("")) {
			sendtofile = "false";
		} else {
			sendtofile = "true"; 
		}
		String targetxsl = "";
		if (fileformat.equalsIgnoreCase("JSON")) {
			targetxsl = "jsonfile.xsl";
		} else if (fileformat.equalsIgnoreCase("unet")) {
			targetxsl = "unetfile.xsl";
		} else if (fileformat.equalsIgnoreCase("jnet")) {
			targetxsl = "jnetfile.xsl";
		} else if (fileformat.equalsIgnoreCase("DOT")) {
			targetxsl = "dotfile.xsl";
		} else if (fileformat.equalsIgnoreCase("cypher")) {
			targetxsl = "cypher.xsl";
		} else if (fileformat.equalsIgnoreCase("NET")) {
			targetxsl = "fullnet.xsl";
		} else if (fileformat.equalsIgnoreCase("commands")) {
			targetxsl = "buildcommands.xsl";
		} else if (fileformat.equalsIgnoreCase("INSERTS")) {
			targetxsl = "inserts.xsl";
		} else if (fileformat.equalsIgnoreCase("TABLES")) {
			targetxsl = "tables.xsl";
		} else if (fileformat.equalsIgnoreCase("NODESLINKS")) {
			targetxsl = "nodesnlinks.xsl";
		}

		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element GraphXMLElem;
		Element GraphElem;
		Element GraphPropElem;
		Element NodeElem;
		Element NodePropElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Element LinkElem;
		Element LinkPropElem;
		String propvalue = "";
		String nodename = "";
		String linkname = "";
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.transformout(doc, "menutop.xsl", response);
			neographworktx = neographdbworking.beginTx();
			// doc = usersession.startNewDoc();
			GraphXMLElem = doc.createElement("GraphXML");
			// doc.appendChild(GraphXMLElem);
			rootElem.appendChild(GraphXMLElem);
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps); // ??? probably dont need
			GraphElem = doc.createElement("Graph");
			GraphXMLElem.appendChild(GraphElem);
			GraphPropElem = doc.createElement("GraphProp");
			GraphElem.appendChild(GraphPropElem);
			GraphPropElem.setAttribute("Name", "Place");
			GraphPropElem.setAttribute("Value", "Holder");
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			// iterate over neonodes
			while (neonodeiter.hasNext()) {
				neonodehold = neonodeiter.next();
				usersession.log.debug("NC:SGT:neonode:id:" + neonodehold.getId());
				NodeElem = doc.createElement("Node");
				GraphElem.appendChild(NodeElem);
				NodeElem.setAttribute("id", Long.toString(neonodehold.getId()));
				try {
					nodename = (String) neonodehold.getProperty("Name");
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					nodename = "Not Found";
					usersession.log.debug("NC:SGT:Nodename not found.:nfe:" + nfe.toString());
				}
				NodeElem.setAttribute("Name", nodename);
				if (neonodehold.getLabels() != null) {
					NodeElem.setAttribute("Label", neonodehold.getLabels().toString());
				}
				NodeElem.setAttribute("numprops", Integer.toString(neonodehold.getAllProperties().size()));
				NodeElem.setAttribute("props", neonodehold.getAllProperties().toString());
				// iterate over properties for this neonode, add the key and value to the
				// gexfnodehole
				neopropiter = neonodehold.getPropertyKeys().iterator();
				while (neopropiter.hasNext()) {
					try {
						neopropkey = neopropiter.next();
						propvalue = (String) neonodehold.getProperty(neopropkey);
						NodePropElem = doc.createElement("NodeProp");
						NodeElem.appendChild(NodePropElem);
						usersession.log.debug("NC:SGT:nodekey:" + neopropkey + " prop:" + propvalue);
						NodePropElem.setAttribute("Name", neopropkey);
						NodePropElem.setAttribute("Value", propvalue);
						NodePropElem.setAttribute("Type", "String");
					} catch (org.neo4j.graphdb.NotFoundException nfe) {
						usersession.log.debug("NC:SGT:nodeprop:nfe:" + nfe.toString());
					}
				}
			}
			// now the relationships / links
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;
			propvalue = "";
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking
					.getAllRelationships().iterator();
			while (neorelationshipiter.hasNext()) {
				neorelationshiphold = neorelationshipiter.next();
				usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:sFF:neostartnodeid:" + neostartnodeid);
				usersession.log.debug("NC:sFF:noendnodeid:" + neoendnodeid);
				LinkElem = doc.createElement("Link");
				GraphElem.appendChild(LinkElem);
				try {
					linkname = (String) neorelationshiphold.getProperty("Name");
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					linkname = "Not Found";
					usersession.log.debug("NC:sFF:Nodename not found.:nfe:" + nfe.toString());
				}
				LinkElem.setAttribute("Name", linkname);
				LinkElem.setAttribute("Id", Long.toString(neorelationshiphold.getId()));
				LinkElem.setAttribute("LeftNodeName", (String) neorelationshiphold.getStartNode().getProperty("Name"));
				LinkElem.setAttribute("RightNodeName", (String) neorelationshiphold.getEndNode().getProperty("Name"));
				LinkElem.setAttribute("numprops", Integer.toString(neorelationshiphold.getAllProperties().size()));
				LinkElem.setAttribute("props", neorelationshiphold.getAllProperties().toString());
				neopropiter = neorelationshiphold.getPropertyKeys().iterator();
				while (neopropiter.hasNext()) {
					try {
						neopropkey = neopropiter.next();
						propvalue = (String) neorelationshiphold.getProperty(neopropkey);
						usersession.log.debug("NC:sFF:edgekey:" + neopropkey + " edgeprop:" + propvalue);
						LinkPropElem = doc.createElement("LinkProp");
						LinkElem.appendChild(LinkPropElem);
						LinkPropElem.setAttribute("Name", neopropkey);
						LinkPropElem.setAttribute("Value", propvalue);
						LinkPropElem.setAttribute("Type", "String");
						if (neopropkey.equalsIgnoreCase("label") && propvalue.trim().length() > 0) {
							LinkElem.setAttribute("label", propvalue.trim());
						}
					} catch (ClassCastException cce) {
						usersession.log.debug("NC:sFF:Class Cast Exception ");
					} catch (org.neo4j.graphdb.NotFoundException nfe) {
						usersession.log.debug("NC:sFF:edgeprop:nfe:" + nfe.toString());
					}
				}
			}
			if (sendtofile.equalsIgnoreCase("true")) {
				usersession.log.debug("NC:sendFormattedFile:sendtofile=true:" );
				String nl = System.lineSeparator();
				
				File dotterfile = new File("/var/www/html/map/dotter.dot");
				Writer dotterout;
				try {
					dotterout = new FileWriter(dotterfile, false);
					dotterout.write(" ");
					String realpath = servletcontextA.getRealPath("/");
					//usersession.log.debug("NC:sendFormattedFile:path:" + realpath);
					String realpathplusstylesheet = realpath + "xslt/en/orig/plaindot.xsl"; 
					//usersession.log.debug("NC:sendFormattedFile:realpathplusstylesheet:" + realpathplusstylesheet);
					File stylesheet = new File(realpathplusstylesheet);					
					TransformerFactory factory = TransformerFactory.newInstance();
					Source streamsource = new StreamSource(stylesheet);
					//usersession.log.debug("NC:sendFormattedFile:after newFile stylesheet, before newTransformer.");
					Transformer transformer = factory.newTransformer(streamsource);
					//usersession.log.debug("NC:sendFormattedFile:before stream result");
					javax.xml.transform.Result response = (Result) new StreamResult(dotterout);
					//usersession.log.debug("NC:sendFormattedFile:before transform result.");
					transformer.transform(new DOMSource(doc), (javax.xml.transform.Result) response);
					//usersession.log.debug("NC:sendFormattedFile:before sendnormalbegin.");
					//sendNormalBegin();
					String[] stringhold = {"/var/www/html/drawnNet.sh"};
					executeProcess(stringhold); // this includes sendNets
					//sendNets();
				} catch (IOException ioe) {
					usersession.log.debug("NC:sendFormattedFile:ioe:" + ioe.toString());
				}
			} else {
				usersession.transformout(doc, targetxsl, response);
			}
		} catch (Exception e) {
			usersession.log.error("NC:sendFormattedFile:e:" + e.toString());
		} finally {
			neographworktx.success();
			usersession.log.debug("NC:sFF:before trans.close(), ending the beginTx.");
			neographworktx.close();
		}
		usersession.log.debug("NC:sendFormattedFile: END  +++++");
	} // sendFormattedFile
	
	public void writeCommands() {
		try {
			usersession.log.debug("writeCommands: before new FileWriter" );
			FileWriter writer = new FileWriter("/var/www/html/example.txt");
			writer.write("hello cruel world.");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			usersession.log.debug("writeCommands:" + e.toString());
		}
	}
	
	/**
	 * sendActiveDemoFile netlistener commands
	 * 20201103 0900 rbg copied from sendFormatted
	 * need a map of nodes added hashmap by name 
	 */
	public void sendActiveDemoFile() throws ServiceException {
		usersession.log.debug("NC:sendActiveDemoFile: BEGIN ++++++++++++");	

		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element GraphXMLElem;
		Element GraphElem;
		Element GraphPropElem;
		Element NodeElem;
		Element NodePropElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Element LinkElem;
		Element LinkPropElem;
		String propvalue = "";
		String leftnodename = "";
		String rightnodename = "";
		String linkname = "";
		StringBuffer sba = new StringBuffer();
		try {
			usersession.log.debug("NC:sendActiveDemoFile:writeCommands: before new FileWriter" );
			FileWriter writer = new FileWriter("/var/www/html/file.in");
			neographworktx = neographdbworking.beginTx();			
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;
			propvalue = "";
			neopropiter = null;
			org.neo4j.graphdb.Node neostartnode = null;
			org.neo4j.graphdb.Node neoendnode = null;
			sba.setLength(0);
			sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"network\", \"name\": \"newnet\"}}\n");
			writer.write(sba.toString());
			sba.setLength(0);
			sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"netprops\", \"netname\": \"newnet\",\"netprops\":[{ \"NetworkKnownAs\": \"Network\", \"NodeKnownAs\": \"Node\", \"LinkKnownAs\": \"Link\", \"version\": \"20200820 1200.\", \"Place\": \"Holder\" }]}}\n");
			writer.write(sba.toString());
			sba.setLength(0);
			
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking
					.getAllRelationships().iterator();
			while (neorelationshipiter.hasNext()) {
				neorelationshiphold = neorelationshipiter.next();				
				usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neostartnode = neorelationshiphold.getStartNode();
				neoendnode = neorelationshiphold.getEndNode();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:sFF:neostartnodeid:" + neostartnodeid);
				
				// add the startnode / leftnode
				usersession.log.debug("NC:sendActiveDemoFile:neonode:id:" + neostartnodeid);
				// NodeElem.setAttribute("id", Long.toString(neostartnodeid));
				try {
					leftnodename = (String) neostartnode.getProperty("Name");
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					leftnodename = "Not Found";
					usersession.log.debug("NC:sendActiveDemoFile:Left Nodename not found.:nfe:" + nfe.toString());
				}
				try {
					rightnodename = (String) neoendnode.getProperty("Name");
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					rightnodename = "Not Found";
					usersession.log.debug("NC:sendActiveDemoFile:Right Nodename not found.:nfe:" + nfe.toString());
				}
				if ( !nodeset.contains(leftnodename) ) { // NOT NOT
					nodeset.add(leftnodename);					
					sba.setLength(0);
					sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"nodes\", ");
					sba.append("\"name\": \"" +  leftnodename + "\"}}");
					sba.append("\n");
					writer.write(sba.toString());
					sba.setLength(0);
					neopropiter = neostartnode.getPropertyKeys().iterator();
					if ( neopropiter.hasNext() ) {
						sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"nprops\", ");
						sba.append("\"nodename\": " + "\"" + leftnodename + "\", ");
						sba.append("\"nprops\":" + "[{ ");
					}
					while (neopropiter.hasNext() ) {
						try {
							neopropkey = neopropiter.next();
							propvalue = (String) neostartnode.getProperty(neopropkey);
							sba.append("\"" + neopropkey + "\": " + "\"" + propvalue + "\"");
							usersession.log.debug("NC:sendActiveDemoFile:nodekey:" + neopropkey + " prop:" + propvalue );
							if (neopropiter.hasNext() ) {
								sba.append(", ");
							}
						} catch ( org.neo4j.graphdb.NotFoundException nfe ) {
							usersession.log.debug("NC:sendActiveDemoFile:nodeprop:nfe:" + nfe.toString());
						}
					}
					sba.append("}]}}\n");
					writer.write(sba.toString());
					sba.setLength(0);
				}
				
				if ( !nodeset.contains(rightnodename) ) { // NOT NOT
					nodeset.add(rightnodename);					
					sba.setLength(0);
					sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"nodes\", ");
					sba.append("\"name\": \"" + rightnodename + "\"}}");
					sba.append("\n");
					writer.write(sba.toString());
					sba.setLength(0);
					neopropiter = neoendnode.getPropertyKeys().iterator();
					if ( neopropiter.hasNext() ) {
						sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"nprops\", ");
						sba.append("\"nodename\": " + "\"" + rightnodename + "\", ");
						sba.append("\"nprops\":" + "[{ ");
					}
					while (neopropiter.hasNext() ) {
						try {
							neopropkey = neopropiter.next();
							propvalue = (String) neostartnode.getProperty(neopropkey);
							sba.append("\"" + neopropkey + "\": " + "\"" + propvalue + "\"");
							usersession.log.debug("NC:sendActiveDemoFile:nodekey:" + neopropkey + " prop:" + propvalue);
							if (neopropiter.hasNext() ) {
								sba.append(", ");
							}
						} catch ( org.neo4j.graphdb.NotFoundException nfe ) {
							usersession.log.debug("NC:sendActiveDemoFile:nodeprop:nfe:" + nfe.toString());
						}
					}
					sba.append("}]}}\n");
					writer.write(sba.toString());
					sba.setLength(0);
				}
				sba.setLength(0);
				sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"links\", ");
				sba.append("\"name\": \"" +  leftnodename + rightnodename + "\", ");
				sba.append("\"leftnode\": \"" + leftnodename + "\", ");
				sba.append("\"rightnode\" : \"" + rightnodename + "\" }}" );
				sba.append("\n");
				writer.write(sba.toString());
				sba.setLength(0);
				
				try {
					linkname = (String) neorelationshiphold.getProperty("Name");
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					linkname = "Not Found";
					usersession.log.debug("NC:sendActiveDemoFile:Nodename not found.:nfe:" + nfe.toString());
				}
				
				neopropiter = neorelationshiphold.getPropertyKeys().iterator();
				if ( neopropiter.hasNext() ) {
					sba.append("{\"command\": {\"fx\": \"add\", \"set\": \"lprops\", ");
					sba.append("\"linkname\":" + "\"" + leftnodename + rightnodename + "\", ");
					sba.append("\"lprops\":" + "[{ ");
				}
				while (neopropiter.hasNext() ) {
					try {
						neopropkey = neopropiter.next();
						propvalue = (String) neorelationshiphold.getProperty(neopropkey);
						sba.append("\"" + neopropkey + "\": " + "\"" + propvalue + "\"");
						usersession.log.debug("NC:sendActiveDemoFile:nodekey:" + neopropkey + " prop:" + propvalue);
						if (neopropiter.hasNext() ) {
							sba.append(", ");
						}
					} catch ( org.neo4j.graphdb.NotFoundException nfe ) {
						usersession.log.debug("NC:sendActiveDemoFile:nodeprop:nfe:" + nfe.toString());
					}
				}
				sba.append("}]}}\n");
				writer.write(sba.toString());
				sba.setLength(0);
			}
			writer.flush();
			writer.close();
		} catch (IOException ioe) {
			usersession.log.debug("NC:sendActiveDemoFile:ioe:" + ioe.toString());		
		} catch (Exception e) {
			usersession.log.error(e.toString());
		} finally {
			neographworktx.success();
			// usersession.log.debug("NC:sendActiveDemoFile:before trans.close(), ending the beginTx.");
			neographworktx.close();
			sendNormalBegin();
		}
		usersession.log.debug("NC:sendActiveDemoFile: END  +++++");
	} // sendActiveDemoFile
	
	/*
	 * references usersession.nettreecontrol.NTNet as nethold
	 * nettreecontrol is in the "key":<key>, "value":<val> format ( not good )
	 * a doc is created and this iterates over the network loading the DOM
	 * transformed here: usersession.transformout(usersession.doc, "controlnet.xsl", response); 
	 */
	public void sendControlNet() throws  ServiceException {
		Net nethold = null;
		Netprop netprophold = null;
		Node nodehold = null;
		Nprop nprophold = null;
		Link linkhold = null;
		Lprop lprophold = null;
		
		Element NetElem;
		Element NetpropElem; 
		Element NodeElem;
		Element NodepropElem;
		Element LinkElem;
		Element LinkpropElem;
		
		String propvalue = "";	
		String nodename = "";
		String linkname = "";
		String npropname = "";
		String lpropname = "";
		String netpropkey = "";
		
		Iterator nodeiter = null;
		Iterator linkiter = null;
		Iterator npropiter = null;
		Iterator lpropiter = null;
		Iterator netpropiter = null;
		
		try {
			nethold = usersession.nettreecontrol.NTNet;
			usersession.log.debug("NC:sendCN:nethold.name:"  + nethold.name);
			usersession.log.debug("NC:sendCN:nethold.netprops.size:"  + nethold.getNetProps().size());
			usersession.log.debug("NC:sendCN:nethold.nodes.size:"  + nethold.getNodes().size());
			usersession.log.debug("NC:sendCN:nethold.links.size:"  + nethold.getLinks().size());
			usersession.log.debug("NC:sendCN:nettreecontrol:"  + usersession.nettreecontrol.linespecsethash.size());
			usersession.log.debug("top of sendControlNet: before Net and Netprops");
			
			Document doc = usersession.startNewDoc();			
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			usersession.genpropman.props2DOM(doc, rootElem, "");
			usersession.transformout(doc, "menutop.xsl", response);
			
			usersession.startNewDoc();			
			NetElem = usersession.doc.createElement("Net");
			usersession.doc.appendChild(NetElem);
			NetElem.setAttribute("name", nethold.name );
			netpropiter = nethold.getNetProps().keySet().iterator();
			while ( netpropiter.hasNext() ) {
				netpropkey = (String) netpropiter.next();
				//usersession.log.debug("NC:sendCN:netpropkey:" + netpropkey);
				netprophold = nethold.getNetProps().get(netpropkey);
				//usersession.log.debug("NC:sendCN:After get.");
				NetpropElem = usersession.doc.createElement("NetProp");
				//usersession.log.debug("NC:sendCN:After create.");
				NetElem.appendChild(NetpropElem);
				//usersession.log.debug("NC:sendCN:After append.");
				NetpropElem.setAttribute("key", netprophold.key);
				NetpropElem.setAttribute("value", netprophold.value);
				//usersession.log.debug("NC:sendCN:After 2 values.");
				if (netprophold.type.trim().contentEquals("") ) {
					NetpropElem.setAttribute("type", "str");				
				} else {
					NetpropElem.setAttribute("type", netprophold.type);			
				}
				//usersession.log.debug("NC:sendCN:After all gets.");
			}
			usersession.log.debug("NC:sendCN:Before Nodes.");
			nodeiter = nethold.getNodes().keySet().iterator();
			while ( nodeiter.hasNext() ) {
				//usersession.log.debug("NC:sendCN:in nodeiter.");
				nodename = (String) nodeiter.next();
				nodehold = nethold.getNodes().get(nodename);
				NodeElem = usersession.doc.createElement("Node"); 
				NetElem.appendChild(NodeElem);
				NodeElem.setAttribute("Name", nodehold.name);
				npropiter = nodehold.nprops.keySet().iterator();
				while ( npropiter.hasNext() ) {			
					//usersession.log.debug("NC:sendCN:in npropiter.");
					npropname = (String)npropiter.next();
					//usersession.log.debug("NC:sendCN:npropname:" + npropname);
					nprophold = nodehold.nprops.get(npropname);
					//usersession.log.debug("NC:sendCN:npropname:" + npropname);
					NodepropElem = usersession.doc.createElement("Nprop");
					NodeElem.appendChild(NodepropElem);
					NodepropElem.setAttribute("key", nprophold.key);
					NodepropElem.setAttribute("value", nprophold.value);
					if (nprophold.type.trim().contentEquals("")) {
						NodepropElem.setAttribute("type", "str");
					} else {
						NodepropElem.setAttribute("type", nprophold.type);
					}
				}				
			}
			//usersession.log.debug("NC:sendCN:Before Links.");
			linkiter = nethold.getLinks().keySet().iterator();
			while ( linkiter.hasNext() ) {
				linkname = (String) linkiter.next();
				linkhold = nethold.getLinks().get(linkname);				
				LinkElem = usersession.doc.createElement("Link"); 
				NetElem.appendChild(LinkElem);
				LinkElem.setAttribute("leftname", linkhold.leftname);
				LinkElem.setAttribute("rightname", linkhold.rightname);
				LinkElem.setAttribute("Name", linkhold.name);
				lpropiter = linkhold.lprops.keySet().iterator();
				while ( lpropiter.hasNext() ) {
					lpropname = (String)lpropiter.next();
					lprophold = linkhold.lprops.get(lpropname);
					LinkpropElem = usersession.doc.createElement("Lprop");
					LinkElem.appendChild(LinkpropElem);
					LinkpropElem.setAttribute("key", lprophold.key);
					LinkpropElem.setAttribute("value", lprophold.value);
					if (nprophold.type.trim().contentEquals("")) {
						LinkpropElem.setAttribute("type", "str");
					} else {
						LinkpropElem.setAttribute("type", lprophold.type);
					}
				}				
			}
			usersession.transformout(usersession.doc, "controlnet.xsl", response); 
		} catch (NetTreeException nte) {
			usersession.log.debug("NTC:LLSMH:nte:code:" + nte.getCode() + " msg:" + nte.getMessage());	
			messenger("SCN:NTE", "SCN:NTE:" + nte.getMessage());
		} catch ( ParserConfigurationException pce ) {
			usersession.log.error(pce.toString());
		} catch ( NullPointerException npe ) {
			usersession.log.error("NC:SCN:npe:" + npe.toString());
		}
	} // sendControlNet() 
	
	/*
	 * copied from sendControlNet
	 */
	public void sendSettingsNet() throws  ServiceException {
		Net nethold = null;
		Netprop netprophold = null;
		Node nodehold = null;
		Nprop nprophold = null;
		Link linkhold = null;
		Lprop lprophold = null;
		
		Element NetElem;
		Element NetpropElem; 
		Element NodeElem;
		Element NodepropElem;
		Element LinkElem;
		Element LinkpropElem;
		
		String propvalue = "";	
		String nodename = "";
		String linkname = "";
		String npropname = "";
		String lpropname = "";
		String netpropkey = "";
		
		Iterator nodeiter = null;
		Iterator linkiter = null;
		Iterator npropiter = null;
		Iterator lpropiter = null;
		Iterator netpropiter = null;
		
		JsonArray netpropsarry = null;
		Iterator<?> netpropsiter = null;
		
		try {
			usersession.log.debug("NC:sendSN:top of sendSettingsNet: before Net and Netprops");
			if ( usersession.settingsnetset ) {					
				nethold = usersession.settingsnet;
				usersession.log.debug("NC:sendSN:nethold.name:"  + nethold.name);
				usersession.log.debug("NC:sendSN:nethold.netprops.size:"  + nethold.getNetProps().size());
				usersession.log.debug("NC:sendSN:nethold.nodes.size:"  + nethold.getNodes().size());
				usersession.log.debug("NC:sendSN:nethold.links.size:"  + nethold.getLinks().size());
				usersession.log.debug("NC:sendSN:netsettings.version:"  + usersession.settingsnet.version);			
				Document doc = usersession.startNewDoc();			
				Element rootElem = doc.createElement("root");
				doc.appendChild(rootElem);
				Element globalElem = doc.createElement("global");
				usersession.genpropman.props2DOM(doc, rootElem, "");
				usersession.transformout(doc, "menutop.xsl", response);			
				usersession.startNewDoc();			
				NetElem = usersession.doc.createElement("Net");
				usersession.doc.appendChild(NetElem);
				NetElem.setAttribute("name", nethold.name );
				netpropiter = nethold.getNetProps().keySet().iterator();
				//netpropiter = nethold.getNetProps().entrySet().iterator();
				usersession.log.debug("NC:sendSN:netpropiter:hasNext:" + netpropiter.hasNext());
				usersession.log.debug("NC:sendSN:netprops:size:" + nethold.getNetProps().size());
				//usersession.log.debug("NC:sendSN:netprops:" + nethold.getNetProps().toString());
				
				while ( netpropiter.hasNext() ) {
					netpropkey = (String) netpropiter.next();
					usersession.log.debug("NC:sendSN:netpropkey:" + netpropkey);					
					netprophold = (Netprop) nethold.getNetprop(netpropkey);
					usersession.log.debug("NC:sendSN:netprophold:value:" + netprophold.value);
					NetpropElem = usersession.doc.createElement("NetProp");
					//usersession.log.debug("NC:sendSN:After create.");
					NetElem.appendChild(NetpropElem);
					//usersession.log.debug("NC:sendSN:After append.");
					NetpropElem.setAttribute("key", netprophold.key);
					NetpropElem.setAttribute("value", netprophold.value);
					NetpropElem.setAttribute("type", "str");				
					//usersession.log.debug("NC:sendSN:After all gets.");
				}
				
				usersession.log.debug("NC:sendSN:Before Nodes: " + nethold.getNodes().size());								
				nodeiter = nethold.getNodes().keySet().iterator();
				
				while ( nodeiter.hasNext() ) {
					//usersession.log.debug("NC:sendSN:in nodeiter.");
					nodename = (String) nodeiter.next();
					nodehold = nethold.getNodes().get(nodename);
					NodeElem = usersession.doc.createElement("Node"); 
					NetElem.appendChild(NodeElem);
					NodeElem.setAttribute("Name", nodehold.name);
					npropiter = nodehold.nprops.keySet().iterator();
					while ( npropiter.hasNext() ) {			
						//usersession.log.debug("NC:sendSN:in npropiter.");
						npropname = (String)npropiter.next();
						//usersession.log.debug("NC:sendSN:npropname:" + npropname);
						nprophold = nodehold.nprops.get(npropname);
						//usersession.log.debug("NC:sendSN:npropname:" + npropname);
						NodepropElem = usersession.doc.createElement("Nprop");
						NodeElem.appendChild(NodepropElem);
						NodepropElem.setAttribute("key", nprophold.key);
						NodepropElem.setAttribute("value", nprophold.value);
						if (nprophold.type.trim().contentEquals("")) {
							NodepropElem.setAttribute("type", "str");
						} else {
							NodepropElem.setAttribute("type", nprophold.type);
						}
					}				
				}
				usersession.log.debug("NC:sendSN:Before Links: "  + nethold.getLinks().size());		
				linkiter = nethold.getLinks().keySet().iterator();
				while ( linkiter.hasNext() ) {
					linkname = (String) linkiter.next();
					linkhold = nethold.getLinks().get(linkname);				
					LinkElem = usersession.doc.createElement("Link"); 
					NetElem.appendChild(LinkElem);
					LinkElem.setAttribute("leftname", linkhold.leftname);
					LinkElem.setAttribute("rightname", linkhold.rightname);
					LinkElem.setAttribute("Name", linkhold.name);
					lpropiter = linkhold.lprops.keySet().iterator();
					while ( lpropiter.hasNext() ) {
						lpropname = (String)lpropiter.next();
						lprophold = linkhold.lprops.get(lpropname);
						LinkpropElem = usersession.doc.createElement("Lprop");
						LinkElem.appendChild(LinkpropElem);
						LinkpropElem.setAttribute("key", lprophold.key);
						LinkpropElem.setAttribute("value", lprophold.value);
						if (nprophold.type.trim().contentEquals("")) {
							LinkpropElem.setAttribute("type", "str");
						} else {
							LinkpropElem.setAttribute("type", lprophold.type);
						}
					}				
				}
				usersession.transformout(usersession.doc, "controlnet.xsl", response); 
			} else {
				usersession.message4dom.messagewords.add("Settings not set properly");
				usersession.message4dom.messagewords.add("settingnetset:" + usersession.settingsnetset);
			}
		} catch (NetTreeException nte) {
			usersession.log.debug("NTC:LLSMH:nte:code:" + nte.getCode() + " msg:" + nte.getMessage());	
			messenger("SCN:NTE", "SCN:NTE:" + nte.getMessage());
		} catch ( ParserConfigurationException pce ) {
			usersession.log.error(pce.toString());
		} catch ( NullPointerException npe ) {
			usersession.log.error("NC:sSN:npe:" + npe.toString());
		}
		
	} // sendSettingsNet()
	
	public void sendManageLinks( ) throws ServiceException {
		usersession.log.debug("NC:sendManageLinks: BEGIN ++++++++++++");		 
		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element GraphXMLElem;
		Element GraphElem;
		Element GraphPropElem;
		Element NodeElem;
		Element NodePropElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Element LinkElem;
		Element LinkPropElem;
		String propvalue = "";	
		String nodename = "";
		String linkname = "";
		String targetxsl= "audiLinkpop.xsl";
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.genpropman.props2DOM(doc, rootElem, ""); 
			usersession.transformout(doc, "menutop.xsl", response); 
			neographworktx = neographdbworking.beginTx();			
			GraphXMLElem = doc.createElement("GraphXML");
			//doc.appendChild(GraphXMLElem);
			rootElem.appendChild(GraphXMLElem);
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps); // ??? probably dont need
			GraphElem = doc.createElement("Graph");
			GraphXMLElem.appendChild(GraphElem);
			GraphPropElem = doc.createElement("GraphProp");
			GraphElem.appendChild(GraphPropElem);
			GraphPropElem.setAttribute("Name", "");
			GraphPropElem.setAttribute("Value", "");
			GraphPropElem.setAttribute("Type", "");
			// now the relationships / links
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;				
			propvalue = "";
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
			while ( neorelationshipiter.hasNext() ) {
				neorelationshiphold = neorelationshipiter.next();
				usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:SGT:neostartnodeid:" + neostartnodeid); 
				usersession.log.debug("NC:SGT:noendnodeid:" + neoendnodeid );	
				LinkElem = doc.createElement("Link");
				GraphElem.appendChild(LinkElem);
				try {
					linkname = (String) neorelationshiphold.getProperty("Name"); 
				} catch (org.neo4j.graphdb.NotFoundException nfe ) {
					linkname = "Not Found";
					usersession.log.debug("NC:SGT:Nodename not found.:nfe:" + nfe.toString());
				}
				LinkElem.setAttribute("Name", linkname);
				LinkElem.setAttribute("Id", Long.toString(neorelationshiphold.getId()));
				LinkElem.setAttribute("LeftNodeName",(String) neorelationshiphold.getStartNode().getProperty("Name"));
				LinkElem.setAttribute("RightNodeName",(String) neorelationshiphold.getEndNode().getProperty("Name"));
				LinkElem.setAttribute("numprops",Integer.toString(neorelationshiphold.getAllProperties().size() ));
			} // while more links
			usersession.transformout(doc, targetxsl, response);
		} catch (Exception e ) {
			usersession.log.error(e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:sendManageLinks: END  +++++");	
	}	// sendManageLinks	
	
	/**
	 * Send a entire Network as tables
	 * 20181120 rbg orig
	 * 20190516 1410 graphml under root
	 */
	public void sendGraphTables( ) throws ServiceException {
		usersession.log.debug("NC:sendGraphTables: BEGIN ++++++++++++");		
		String targetxsl = "graph.xsl";		
		String neopropkey;
		Iterator<String> neopropiter = null;
		org.neo4j.graphdb.Node neonodehold = null;
		Element GraphXMLElem;
		Element GraphElem;
		Element GraphPropElem;
		Element NodeElem;
		Element NodePropElem;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		Element LinkElem;
		Element LinkPropElem;
		String propvalue = "";	
		String nodename = "";
		String linkname = "";
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.genpropman.props2DOM(doc, rootElem, ""); 
			usersession.transformout(doc, "menutop.xsl", response); 
			neographworktx = neographdbworking.beginTx();			
			// doc = usersession.startNewDoc(); 
			GraphXMLElem = doc.createElement("GraphXML");
			//doc.appendChild(GraphXMLElem);
			rootElem.appendChild(GraphXMLElem);
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps); // ??? probably dont need
			GraphElem = doc.createElement("Graph");
			GraphXMLElem.appendChild(GraphElem);
			GraphPropElem = doc.createElement("GraphProp");
			GraphElem.appendChild(GraphPropElem);
			GraphPropElem.setAttribute("Name", "");
			GraphPropElem.setAttribute("Value", "");
			GraphPropElem.setAttribute("Type", "");
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			// iterate over neonodes
			while ( neonodeiter.hasNext() ) {
				neonodehold = neonodeiter.next();
				usersession.log.debug("NC:SGT:neonode:id:" + neonodehold.getId() );				
				NodeElem = doc.createElement("Node");
				GraphElem.appendChild(NodeElem);
				try {
					nodename = (String) neonodehold.getProperty("Name"); 
				} catch (org.neo4j.graphdb.NotFoundException nfe ) {
					nodename = "Not Found";
					usersession.log.debug("NC:SGT:Nodename not found.:nfe:" + nfe.toString());
				}
				NodeElem.setAttribute("Name", nodename);
				NodeElem.setAttribute("id", Long.toString(neonodehold.getId()));
				if ( neonodehold.getLabels() != null ) {
					NodeElem.setAttribute("Label", neonodehold.getLabels().toString());
				}
				NodeElem.setAttribute("numprops", Integer.toString(neonodehold.getAllProperties().size( )));
				NodeElem.setAttribute("props", neonodehold.getAllProperties().toString());
				// iterate over properties for this neonode, add the key and value to the gexfnodehole
				neopropiter = neonodehold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {
					try {
						neopropkey = neopropiter.next();
						propvalue = (String) neonodehold.getProperty(neopropkey);
						NodePropElem = doc.createElement("NodeProp");
						NodeElem.appendChild(NodePropElem);
						usersession.log.debug("NC:SGT:nodekey:" + neopropkey + " prop:" + propvalue);
						NodePropElem.setAttribute("Name", neopropkey);						
						NodePropElem.setAttribute("Value", propvalue);
						NodePropElem.setAttribute("Type", "String");
					} catch (org.neo4j.graphdb.NotFoundException nfe ) {
						usersession.log.debug("NC:SGT:nodeprop:nfe:" + nfe.toString());
					}
				}
			}			
			// now the relationships / links
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;				
			propvalue = "";
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
			while ( neorelationshipiter.hasNext() ) {
				neorelationshiphold = neorelationshipiter.next();
				usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId();
				usersession.log.debug("NC:SGT:neostartnodeid:" + neostartnodeid); 
				usersession.log.debug("NC:SGT:noendnodeid:" + neoendnodeid );	
				LinkElem = doc.createElement("Link");
				GraphElem.appendChild(LinkElem);
				try {
					linkname = (String) neorelationshiphold.getProperty("Name"); 
				} catch (org.neo4j.graphdb.NotFoundException nfe ) {
					linkname = "Not Found";
					usersession.log.debug("NC:SGT:Nodename not found.:nfe:" + nfe.toString());
				}
				LinkElem.setAttribute("Name", linkname);
				LinkElem.setAttribute("Id", Long.toString(neorelationshiphold.getId()));
				LinkElem.setAttribute("LeftNodeName",neostartnodeid.toString());
				LinkElem.setAttribute("RightNodeName",neoendnodeid.toString());
				LinkElem.setAttribute("numprops",Integer.toString(neorelationshiphold.getAllProperties().size() ));
				LinkElem.setAttribute("props",neorelationshiphold.getAllProperties().toString());
				neopropiter = neorelationshiphold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {	
					try {
						neopropkey = neopropiter.next();		
						propvalue = (String) neorelationshiphold.getProperty(neopropkey);
						usersession.log.debug("NC:SGT:edgekey:" + neopropkey + " edgeprop:" + propvalue);						
						LinkPropElem = doc.createElement("LinkProp");
						LinkElem.appendChild(LinkPropElem);						
						LinkPropElem.setAttribute("Name", neopropkey);
						LinkPropElem.setAttribute("Value", propvalue);
						LinkPropElem.setAttribute("Type", "String");
					} catch	(ClassCastException cce ) {
						usersession.log.debug("NC:SGT:Class Cast Exception ");
					} catch (org.neo4j.graphdb.NotFoundException nfe ) {
						usersession.log.debug("NC:SGT:edgeprop:nfe:" + nfe.toString());
					}
				}
			}
			usersession.transformout(doc, targetxsl, response);
		} catch (Exception e ) {
			usersession.log.error(e.toString());
		} finally {
			neographworktx.success();
			//usersession.log.debug("NC:SGT:before trans.close(), ending the beginTx.");
			neographworktx.close();
		}
		usersession.log.debug("NC:sendDrawnGraph: END  +++++");	
	}	// sendGraphTables 
	
	// this has sendNormalEnd 20190419
	public void writeGexf(GraphDatabaseService neographdbworkingI )   throws ServiceException {
		GraphDatabaseService neographdbworking = neographdbworkingI;
		usersession.log.debug("NC:WGexf: +++++++++++++++++++++++");
		neographworktx = neographdbworking.beginTx();
		// create a gexf implementation and gexf Graph
		Gexf gexf = new GexfImpl();
		Calendar date = Calendar.getInstance();	
		gexf.getMetadata()
			.setLastModified(date.getTime())
			.setCreator("Bob Garvey")
			.setDescription("Output Gexf");
		//gexf.setVisualization(true);
		it.uniroma1.dis.wsngroup.gexf4j.core.Graph gexfgraph = gexf.getGraph();
		gexfgraph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);
										
		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		gexfgraph.getAttributeLists().add(attrList);		
		Map<String, Attribute> attrMap = new HashMap<>();
		Map<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node> gexfNodeMap = new HashMap<>();
		Attribute attrhold = null; 
		String propkey = "";
		String neopropkey = "";
		Iterator<String> neopropertyiter = neographdbworking.getAllPropertyKeys().iterator();
		// load all the property names in attrMap (attribute map)
		while ( neopropertyiter.hasNext() ) {
			propkey = neopropertyiter.next();
			// this is where id -> idx was
			attrhold = attrList.createAttribute(propkey, AttributeType.STRING, propkey);
			usersession.log.debug("attribute:id:" + attrhold.getId());
			attrMap.put(propkey,attrhold);
		}		
		org.neo4j.graphdb.Node neonodehold = null;
		String propvalue = "";
		it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfnodehold = null;
		Iterator<String> neopropiter = null;
		ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
		// iterate over neonodes, create a gexf node with id of neonode, put it in the gefxnodemap
		while ( neonodeiter.hasNext() ) {
			neonodehold = neonodeiter.next();
			usersession.log.debug("neonode:id:" + neonodehold.getId() + " before gexfgraph.createNode.");
			gexfnodehold = gexfgraph.createNode(Long.toString(neonodehold.getId()));
			//gexfnodehold = gexfgraph.createNode();
			usersession.log.debug("gexfnodehold.getId:" + gexfnodehold.getId());
			usersession.log.debug("gexfnodehold.getLabel:" + gexfnodehold.getLabel());
			gexfNodeMap.put(gexfnodehold.getId(), gexfnodehold);
			//gexfnodehold.setLabel(Long.toString(neonodehold.getId()));
			neopropiter = neonodehold.getPropertyKeys().iterator();
			// iterate over properties for this neonode, add the key and value to the gexfnodehole 
			while ( neopropiter.hasNext() ) {
				neopropkey = neopropiter.next();
				usersession.log.debug("key:" + neopropkey + " prop:" + neonodehold.getProperty(neopropkey));
				// ?? PropertyContainer propcontainer = (PropertyContainer) neonodehold.getProperty(neopropkey);
				propvalue = (String) neonodehold.getProperty(neopropkey);
				// ?? id 2 idx was here
				gexfnodehold.getAttributeValues().addValue(attrMap.get(neopropkey), propvalue);
			}
		}
		
		// now the relationships / links
		Long neostartnodeid = (long) 0;
		Long neoendnodeid = (long) 0;	
		String gexfstartnodeid = "";
		String gexfendnodeid = "";
		it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfstartnode = null;
		it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfendnode = null;
		org.neo4j.graphdb.Relationship neorelationshiphold = null;
		propvalue = "";
		it.uniroma1.dis.wsngroup.gexf4j.core.Edge gexfedgehold = null;
		neopropiter = null;
		// iterate of the neorelationships, create gexfedges and properties
		ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
		while ( neorelationshipiter.hasNext() ) {
			neorelationshiphold = neorelationshipiter.next();
			usersession.log.debug("edge:id:" + neorelationshiphold.getId());
			neostartnodeid = neorelationshiphold.getStartNode().getId();
			neoendnodeid = neorelationshiphold.getEndNode().getId();
			gexfstartnodeid = neostartnodeid.toString();
			gexfendnodeid = neoendnodeid.toString();
			gexfstartnode = gexfgraph.getNode(gexfstartnodeid);
			gexfendnode = gexfgraph.getNode(gexfendnodeid);
			usersession.log.debug("WGX:before connectTo:");
			usersession.log.debug(" neostartnodeid:" + neostartnodeid); 
			usersession.log.debug(" noendnodeid:" + neoendnodeid );
			usersession.log.debug(" gexfstartnodeid:" + gexfstartnodeid); 
			usersession.log.debug(" gexfstartnode:" + gexfstartnode); 
			usersession.log.debug(" gexfendnode:"  + gexfendnode);
			// ?? do i need the id of the link
			//gexfedgehold = gexfstartnode.connectTo(Long.toString(neorelationshiphold.getId()), gexfendnode);
			gexfedgehold = gexfstartnode.connectTo(gexfendnode);
			neopropiter = neorelationshiphold.getPropertyKeys().iterator();
			while ( neopropiter.hasNext() ) {
				neopropkey = neopropiter.next();
				usersession.log.debug("edgekey:" + neopropkey + " edgeprop:" + neorelationshiphold.getProperty(neopropkey));
				propvalue = (String) neorelationshiphold.getProperty(neopropkey);
				// this is where Id to IDX was
				gexfedgehold.getAttributeValues().addValue(attrMap.get(neopropkey), propvalue);
			}
		}		
		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File("/var/www/html/map/sample.gexf");
		Writer out;
		try {
			out =  new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");			
		} catch (IOException e) {
			usersession.log.error(e.toString());
		}
		neographworktx.success();
		usersession.log.debug("NC:writeGexf:before trans.close(), ending the beginTx.");
		neographworktx.close();		
		this.sendNormalEnd();
	} // writeGexf

	// this has sendNormalEnd
	public void writeDot(GraphDatabaseService neographdbworkingI, String fileI )  throws ServiceException {
		GraphDatabaseService neographdbworking = neographdbworkingI;
		usersession.log.debug("NC:WD: +++++++++++++++++++++++");
		String nl = System.lineSeparator();
		neographworktx = neographdbworking.beginTx();
		File f = new File("/var/www/html/map/" + fileI + ".dot");
		Writer dout;
		try {
			dout =  new FileWriter(f, false);
			/* courtesy Ian Darwin and Geoff Collyer, Softquad Inc. */
			sbA.setLength(0);
			sbA.append("/* NetTree - Bob Garvey */" + nl);
			sbA.append(" digraph net { " + nl);
			dout.write(sbA.toString());

			String propkey = "";
			String neopropkey = "";
			Iterator<String> neopropertyiter = neographdbworking.getAllPropertyKeys().iterator();
			// load all the property names 
			/*
			while ( neopropertyiter.hasNext() ) {
				propkey = neopropertyiter.next();
				dout.write("WD:propkey:" + propkey );
			}	
			*/	
			org.neo4j.graphdb.Node neonodehold = null;
			String propvalue = "";
			it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfnodehold = null;
			Iterator<String> neopropiter = null;
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			// iterate over neonodes, create a gexf node with id of neonode, put it in the gefxnodemap
			sbA.setLength(0);
			while ( neonodeiter.hasNext() ) {
				neonodehold = neonodeiter.next();
				usersession.log.debug("WD:neonode:id:" + neonodehold.getId());				
				usersession.log.debug("WD:neonode:labels:" + neonodehold.getLabels());
				//sbA.append(neonodehold.getId() + " [label=\"" + neonodehold.getLabels() + "\"];" + nl);
				sbA.append(neonodehold.getId() + " [label=\"" + neonodehold.getProperty("Name") + "\"];" + nl);
				neopropiter = neonodehold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {
					neopropkey = neopropiter.next();
					usersession.log.debug("WD:key:" + neopropkey + " prop:" + neonodehold.getProperty(neopropkey) + "\n");
					propvalue = (String) neonodehold.getProperty(neopropkey);					
				}
			}
			dout.write(sbA.toString());
			// now the relationships / links
			Long neostartnodeid = (long) 0;
			Long neoendnodeid = (long) 0;	
			String gexfstartnodeid = "";
			String gexfendnodeid = "";
			it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfstartnode = null;
			it.uniroma1.dis.wsngroup.gexf4j.core.Node gexfendnode = null;
			org.neo4j.graphdb.Relationship neorelationshiphold = null;
			propvalue = "";
			it.uniroma1.dis.wsngroup.gexf4j.core.Edge gexfedgehold = null;
			neopropiter = null;
			// iterate of the neorelationships, create gexfedges and properties
			sbA.setLength(0);
			ResourceIterator<org.neo4j.graphdb.Relationship> neorelationshipiter = neographdbworking.getAllRelationships().iterator();
			while ( neorelationshipiter.hasNext() ) {
				neorelationshiphold = neorelationshipiter.next();
				usersession.log.debug("edge:id:" + neorelationshiphold.getId());
				neostartnodeid = neorelationshiphold.getStartNode().getId();
				neoendnodeid = neorelationshiphold.getEndNode().getId(); 
				sbA.append(neostartnodeid + " -> " + neoendnodeid + ";" + nl);
				usersession.log.debug("WD:before connectTo:");
				usersession.log.debug(" neostartnodeid:" + neostartnodeid); 
				usersession.log.debug(" noendnodeid:" + neoendnodeid );

				neopropiter = neorelationshiphold.getPropertyKeys().iterator();
				while ( neopropiter.hasNext() ) {
					neopropkey = neopropiter.next();
					usersession.log.debug("key:" + neopropkey + " value:" + neorelationshiphold.getProperty(neopropkey));
					propvalue = (String) neorelationshiphold.getProperty(neopropkey);
				}
			}		
			dout.write(sbA.toString());
			sbA.setLength(0);
			sbA.append("}\n");
			dout.write(sbA.toString());
			dout.flush();
			dout.close();
		} catch (IOException e) {
			usersession.log.error(e.toString());
		}
		neographworktx.success();
		usersession.log.debug("NC:writeDot:before trans.close(), ending the beginTx.");
		neographworktx.close();		
		this.sendNormalEnd();
	} // writeDot
	


	public void sendNodeAddRequest() throws ServiceException {
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps);
			usersession.genpropman.props2DOM(doc, rootElem, "");
			String target = "addnode.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(doc, rootElem );
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			throw new ServiceException(this.usersession, request, "NavCon:sendNodeAddRequest:" + ":ex:" + ex.toString(), ex);
		}
	} // sendNodeAddRequest
	

	public void sendNoMethod() throws ServiceException {
		try {
			Document doc = usersession.startNewDoc();			
			Element rootElem = doc.createElement("root");
			String target = "nomethod.xsl";
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);			
			usersession.genpropman.props2DOM(doc, rootElem, "");
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(doc, rootElem );
			Enumeration paramNames = request.getParameterNames();	
			int paramcount = 0;
			String bufA, bufB;
			while (paramNames.hasMoreElements()) {
				paramcount++;
				bufA = (String) paramNames.nextElement();
				bufB = request.getParameter(bufA).trim();
				messenger("NC:snm:", "ParameterName:" + bufA + "="	+ bufB);
			}
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			throw new ServiceException(this.usersession, request, "NavCon:sendNodeAddRequest:" + ":ex:" + ex.toString(), ex);
		}
	} // sendNodeAddRequest
	
	public void sendNewGraphRequest() throws ServiceException {
		try {
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "newgraph.xsl";
			// action="?service=NavCon&amp;fx=newgraphrequest"
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			addAnyMessage2DOM(usersession.doc, rootElem );	
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			throw new ServiceException(this.usersession, request, "NavCon:sendNewGraphRequest:" + ":ex:" + ex.toString(), ex);
		}
	} // sendNewGraphRequest

	/*
	 * 20190526 adding node selection
	 */
	public void sendLinkAddRequest() throws ServiceException {
		try {
			usersession.log.debug("NC:sendLinkAddRequest:top.");
			neographworktx = neographdbworking.beginTx();

			String node = request.getParameter("node");
			String target = "addlink.xsl";
			Document doc = usersession.startNewDoc();
			org.neo4j.graphdb.Node neonodehold = null;
			Element NodeElem;
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			//usersession.genpropman.loadTemplate(usersession.genpropman.templetProps);
			usersession.genpropman.props2DOM(doc, rootElem, "");
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			usersession.transformout(doc, "menutop.xsl", response);
			ResourceIterator<org.neo4j.graphdb.Node> neonodeiter = neographdbworking.getAllNodes().iterator();
			// iterate over neonodes
			while ( neonodeiter.hasNext() ) {
				neonodehold = neonodeiter.next();
				usersession.log.debug("NC:SDG:neonode:id:" + neonodehold.getId() );				
				NodeElem = doc.createElement("Node");
				rootElem.appendChild(NodeElem);
				NodeElem.setAttribute("id", Long.toString(neonodehold.getId()));
				if ( neonodehold.hasProperty("name" ) ) {
					NodeElem.setAttribute("Name", neonodehold.getProperty("name").toString());
				} else 	if ( neonodehold.hasProperty("Name" ) ) {
					NodeElem.setAttribute("Name", neonodehold.getProperty("Name").toString());
				} else 	if ( neonodehold.getLabels() != null ) {
					NodeElem.setAttribute("Name", neonodehold.getLabels().toString());
				}				
				NodeElem.setAttribute("numprops", Integer.toString(neonodehold.getAllProperties().size( )));
				NodeElem.setAttribute("properties", neonodehold.getAllProperties().toString() );
			}	
			usersession.transformout(doc, target, response); // target = "addlink.xsl";
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:sendLinkAddRequest:nfe:" + nfe.toString());
		} catch (Exception e) {
			usersession.log.error("NavCon:sendLinkAddRequest:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:sendLinkAddRequest:e:" + e.toString());
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
	} // sendLinkAddRequest
	
	/*
	 * 
	 */
	public void contract2Level() throws ServiceException {
		try {
			usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("mode", "normal");

			neographworktx = neographdbworking.beginTx();
			if (nettreeA != null) {
				usersession.log.debug("NC:C2L: nettree != null");
				org.neo4j.graphdb.Node nodehold = nettreeA.startNode;
				NetLine netlinework;
				Iterator<NetLine> linesListIter = nettreeA.linesList.iterator();
				while ( linesListIter.hasNext() ) {					
					netlinework = linesListIter.next();
					usersession.log.debug("NC:C2L: after next()");
					if ( netlinework.level >= usersession.maxlevel ) { 
						usersession.log.debug("NC:C2L: level > maxlevel:" + netlinework.level);						
						netlinework.plusminusneither = PLUS;						
						this.branchContract(netlinework, nettreeA, out);
					} else {
						usersession.log.debug("NC:C2L: not >= usersession.maxlevel.");
					}
				}
			} else {
				messenger("NC:C2L", "NetTree == null.");
				usersession.log.debug("NC:SL:NetTree == null.");
			}
			
		} catch (Exception ex) {
			usersession.log.error("NC:C2L:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:C2L:ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
		usersession.log.debug("NC:C2L:bottom.");
		sendNormalBegin();
	} // contract2Level

	/**
	 * recursive method to contract this section of the spanning tree using NetTree/NetLine
	 */
	private void branchContract(NetLine netlineholdI, NetTree nettreeI, PrintWriter out) {
		usersession.log.debug("NavCon:branchContract:top");
		usersession.log.debug("NavCon:branchContract:hyperLineV.size():" + netlineholdI.hyperLineV.size());
		usersession.log.debug("NavCon:branchContract:before hyper loop.");
		for (Enumeration hyperlineEnum = netlineholdI.hyperLineV.elements(); hyperlineEnum.hasMoreElements();) {
			NetLine hyperlinehold = (NetLine) hyperlineEnum.nextElement();
			// leave the parent/adjacent line visible
			if (hyperlinehold != netlineholdI.adjacentlineref) {
				hyperlinehold.visible = false;
				branchContract(hyperlinehold, nettreeI, out);
			} else {
				usersession.log.debug("NavCon:branchContract:else:hyperlinehold==adjacentlineref");
			}
		}
		usersession.log.debug("NavCon:branchContract:hypoLineV.size():" + netlineholdI.hypoLineV.size());
		usersession.log.debug("NavCon:branchContract:before hypo loop.");
		for (Enumeration hypolineEnum = netlineholdI.hypoLineV.elements(); hypolineEnum.hasMoreElements();) {
			NetLine hypolinehold = (NetLine) hypolineEnum.nextElement();
			// leave the parent/adjacent line visible
			if (hypolinehold != netlineholdI.adjacentlineref) {
				hypolinehold.visible = false;
				branchContract(hypolinehold, nettreeI, out);
			} else {
				usersession.log.debug("NavCon:branchContract:else:HYPOlinehold==adjacentlineref");
			}
		}
	} // branchContract

	
	/**
	 * recursive method to Expand this section of the spanning tree using NetTree
	 * @todo screwed up and deleted this and now must be reworked
	 */
	public void branchExpand(NetLine netlineholdI, NetTree nettreeI, PrintWriter out) {
		for (Enumeration hl = netlineholdI.hyperLineV.elements(); hl.hasMoreElements();) {
			NetLine hlinehold = (NetLine) hl.nextElement();
			// leave the parent/adjacent line visible
			if (hlinehold != netlineholdI.adjacentlineref) {
				hlinehold.visible = true;
				if (hlinehold.plusminusneither != PLUS) {
					branchExpand(hlinehold, nettreeI, out);
				}
			}
		} // endfor hyperlines
		for (Enumeration pl = netlineholdI.hypoLineV.elements(); pl.hasMoreElements();) {
			NetLine plinehold = (NetLine) pl.nextElement();
			// leave the parent/adjacent line visible
			if (plinehold != netlineholdI.adjacentlineref) {
				plinehold.visible = true;
				if (plinehold.plusminusneither != PLUS) {
					branchExpand(plinehold, nettreeI, out);
				}
			}
		} // endfor hypolines
	} // branchExpand()

	/**
	 * The method provides a general status of a service.
	 */
	public String getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the amount of time in minutes left before this service times out. If
	 * negative then continuous.
	 */
	public int getTimeToLive() {
		return this.timeToLive;
	}

	/**
	 * sets the amount of time left before this service times out
	 */
	public void setTimeToLive(int ttl) {
		this.timeToLive = ttl;
	}

	public void sendAreaTemplets() throws ServiceException {
		try {			
			String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			Source source = new StreamSource(new File(serverfilepath + "xslt/en/orig/areatemplets.xsl"));
			Source templets = new StreamSource(new File(serverfilepath + "areatemplets.xml"));
			TransformerFactory factory = TransformerFactory.newInstance();
			Document doc = usersession.startNewDoc();
			Element globalElem = usersession.doc.createElement("global");
			usersession.doc.appendChild(globalElem);
			usersession.transformout(usersession.doc, "menutop.xsl", response);		
			doc = usersession.startNewDoc();
			Element areaElem = doc.createElement("area");
			doc.appendChild(areaElem);
			usersession.log.debug("NC:sendAreaTemplets:before new transformer");
			response.setContentType("text/html");
			PrintWriter printwriterA = response.getWriter();
			usersession.log.debug("NC:sendAreaTemplets: after new transformer; before transformer");	
		    Transformer transformer = factory.newTransformer(source);
		    usersession.log.debug("NC:sendAreaTemplets: after new transformer; before transform");	
		    transformer.transform( templets, new StreamResult( printwriterA ) );		    	  
		} catch (Exception ex) {
			usersession.log.error("NC:sendAreaTemplets:ex:" + usersession.stack2string(ex));
			throw new ServiceException(this.usersession, request, "NC:sendAreaTemplets:" + ":ex:" + ex.toString(), ex);
		}
	} // sendAreaTemplets

	/*
	 * sendNewGraphRequest output form 20190326 newgraph.xsl
	 * action="?service=NavCon&amp;fx=newgraphrequest" todo drop node collection
	 * information and send message that graph is created
	 */
	public void newgraphrequest() throws ServiceException {
		String graphfilename = request.getParameter("graphfilename");
		String nodename = request.getParameter("firstnodename");
		String label = request.getParameter("label");
		String idname = request.getParameter("idname");
		// sendNewGraph(graphfilename, nodename, idname, label);
		try {
			usersession.log.debug("NavCon:sendNewGraph:top.");
			neographworktx = neographdbworking.beginTx();
			Properties ntgraphprops = new Properties();
			ntgraphprops.put("dummy", "dummy");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:NGR:after deleteAll.");
			
			
			usersession.log.debug("NC:NGR:before new NetTree.");
			this.nettreeA = new NetTree(this.neographdbworking, null, ntgraphprops, this.usersession);
			usersession.log.debug("NavCon:NGR:after new nettree.");
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Go:NavCon:NGR:NumberFormatException" + nfe.toString());
		} finally {
			neographworktx.success();			
			neographworktx.close();
		}
		messenger("NC:NGP", "New graph created. Select Node -> Add Node");
		sendNormalBegin();
	} // newgraphrequest

	/**
	 * The method returns a Properties instance which records the position of a
	 * service. An example of a Position would the followin property pairs for a
	 * name and address system: id/1000204, lastname/Jones, firstname/John, add1/104
	 * Walnut.
	 * 
	 * The Session may use these properties to present general position information
	 * for the other services called. Other means services that were not passed the
	 * request.
	 * 
	 * The Service is responsible for maintaining its position properties instance.
	 */
	public Properties getPosition() {
		return position;
	}

	/**
	 * Provides a control interface to a Session. The commands to be implemented
	 * are:
	 * <UL>
	 * <LI>Shutdown - to post any outstanding activity prior to shutting down the
	 * Session
	 * <LI>Set - used with properties to set configuration values in a generalized
	 * way so this interface is not under constant revision. For example a color
	 * scheme could be set by issuing a set with a properties instance containing
	 * general/pastel, warning/dark ....
	 * </UL>
	 */
	public void control(String command, Properties properties) {
		usersession.words.add("NavCon: Control not implemented.");
	}

	/**
	 * Opens a new file at a url and returns a FileWriter
	 * 
	 * @param inputfile
	 * @return FileWriter
	 * @throws ServiceException
	 */
	public FileWriter openTempfile(String inputfile) throws ServiceException {
		FileWriter outB;
		File to_file;
		int randomnum = Math.abs(new Random(System.currentTimeMillis()).nextInt());
		try {
			// tempfilename = ( "temp/" + randomnum + ".xml" );
			if (inputfile != null && inputfile.trim().length() > 0) {
				tempfilename = (inputfile);
			} else {
				tempfilename = (randomnum + ".xml");
			}
			tempfileurl = tempfilename;
			usersession.log.debug("NavCon:openTempFile:tempfileurl:" + tempfileurl);
			to_file = new File(usersession.servletcontextA.getRealPath("/") + tempfilename);
			outB = new FileWriter(to_file);
			return outB;
		} catch (IOException ioe) {
			throw new ServiceException("NavCon:opentempfile:ioe:" + ioe.toString(), ioe);
		}
	} // openTempfile()
	
	// this has sendNormalEnd
	public void sendFileList() throws FileNotFoundException, ServiceException {
		try {
			usersession.log.debug("NC:sendFileList():top");
			File tempDir = new File(usersession.propsmgr.getConfigEntry("filelist.path"));
			usersession.log.error("NC:sendFileList():after createElement:getPath:"
					+ usersession.propsmgr.getConfigEntry("filelist.path"));
			boolean recurse = (usersession.propsmgr.getConfigEntry("filelist.recurse").equalsIgnoreCase("true")) ? true
					: false;
			usersession.log.error("NC:sendFileList():recurse:" + recurse);
			List files = getFileListing(tempDir, recurse);
			this.sendNormalBegin();
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			String target = "filelist.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			usersession.log.error("NC:sendFileList():after createElement:global");
			Element listElem = doc.createElement("list");
			rootElem.appendChild(listElem);
			usersession.log.error("NC:sendFileList():after createElement:list");
			Element fileElem;
			String nextfile = "";
			Iterator filesIter = files.iterator();
			while (filesIter.hasNext()) {
				nextfile = filesIter.next().toString();
				usersession.log.debug("NavCon:sendFileList:iter:" + nextfile);
				if (nextfile.endsWith(".xml") || nextfile.endsWith(".XML")) {
					fileElem = doc.createElement("file");
					listElem.appendChild(fileElem);
					usersession.log.error("NC:sendFileList():after createElement:file");
					addElem(doc, fileElem, "name", nextfile);
					usersession.log.error("NC:sendFileList():after addElem name");
				}
			}
			usersession.transformout(doc, target, response);
			this.sendNormalEnd();
		} catch (Exception ex) {
			usersession.log.error("NC:sendFileList():throwing ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:sendFileList:" + ":ex:" + ex.toString(), ex);
		}
	}  //   sendFileList(

	/**
	 * Recursively walk a directory tree and return a List of all Files found; the
	 * List is sorted using File.compareTo.
	 * 
	 * @param aStartingDir
	 *            is a valid directory, which can be read.
	 */
	List getFileListing(File aStartingDir, boolean recurse) throws FileNotFoundException, ServiceException {
		validateDirectory(aStartingDir);
		List result = new ArrayList();
		File[] filesAndDirs = aStartingDir.listFiles();
		List filesDirs = Arrays.asList(filesAndDirs);
		Iterator filesIter = filesDirs.iterator();
		File file = null;
		while (filesIter.hasNext()) {
			file = (File) filesIter.next();
			result.add(file); // always add, even if directory
			if (recurse) {
				if (!file.isFile()) { // must be a directory
					// ! recursive call !
					List deeperList = getFileListing(file, true);
					result.addAll(deeperList);
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * Directory is valid if it exists, does not represent a file, and can be read.
	 */
	private void validateDirectory(File aDirectory) throws FileNotFoundException, ServiceException {
		usersession.log.debug("NC:validateDirectory:top");
		if (aDirectory == null) {
			usersession.log.debug("NC:validateDirectory:Directory should not be null");
			// throw new ServiceException("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
			usersession.log.debug("NC:validateDirectory:Directory does not exist");
			// throw new ServiceException("Directory does not exist: " +
			// aDirectory);
		}
		if (!aDirectory.isDirectory()) {
			usersession.log.debug("NC:validateDirectory:Is not a directory");
			// throw new ServiceException("Is not a directory: " + aDirectory);
		}
		if (!aDirectory.canRead()) {
			usersession.log.debug("NC:validateDirectory:Directory cannot be read:");
			// throw new ServiceException("Directory cannot be read: " +
			// aDirectory);
		}
		usersession.log.debug("NC:validateDirectory:bottom");
	}

	private void addElem(Document doc, Element parent, String elemName, String elemValue) {
		try {
			Element elem = doc.createElement(elemName);
			elem.appendChild(doc.createTextNode(elemValue));
			parent.appendChild(elem);
		} catch (org.w3c.dom.DOMException dome) {
			usersession.log.debug("DOMEx:elemName:" + elemName + " elemValue:" + elemValue);
		}
	}

	public void sendDrawGraph() throws ServiceException {
		String mastermapname = request.getParameter("mastermap");
		try {
			usersession.log.debug("NavCon:sendDrawGraph:before Runtime");
			Runtime rt = Runtime.getRuntime();
			// String[] commands = {"ls"," > ", "ls.txt"};
			String commands = "ls ps";
			Process proc = rt.exec(commands.split(" "));
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				usersession.log.debug("NavCon:sendDrawGraph:after Runtime:line:" + line);
			}
			usersession.log.debug("NavCon:sendDrawGraph:after Runtime");
		} catch (Exception e) {
			usersession.log.error("NavCon:sendDrawGraph:e:" + usersession.stack2string(e));
			throw new ServiceException("NavCon:sendDrawGraph:sdg:" + e.toString());
		}
	}
	
	public void executeProcess() throws IOException, InterruptedException, ServiceException {		
		String command = request.getParameter("fx1");
		String[] commands = { command };
		executeProcess(commands);
	}	

	public void executeProcess(String[] commands) throws ServiceException {
		// this could be set to a specific directory, if desired
		File directory = null;
		BufferedReader is = null;
		BufferedReader es = null;
		try {
			Process process;
			if (directory != null) {
				process = Runtime.getRuntime().exec(commands, null, directory);
			} else {
				process = Runtime.getRuntime().exec(commands);
			}
			String line;
			is = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = is.readLine()) != null)
				usersession.log.debug("NC:EP:line:" + line);
			es = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = es.readLine()) != null) {
				usersession.log.error("NC:EP:Error:" + line);
			}
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				usersession.log.debug("NC:EP:" + "It worked");
			} else {
				usersession.log.debug("NC:EP:" + " Something bad happend. Exit code: " + exitCode);
			}
		} catch (Exception e) {
			usersession.log.debug("NC:EP:" + "Something when wrong: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					usersession.log.debug("NC:EP:" + "Something whent wrong:is: " + ioe.getMessage());
				}
			}
			if (es != null) {
				try {
					es.close();
				} catch (IOException ioe) {
					usersession.log.debug("NC:EP:" + "Something when wrong:ds: " + ioe.getMessage());
				}
			}
		}
		this.sendNets();
	}

	public void formgen() throws IOException, ServiceException {
		String form = request.getParameter("form");
		formgen(form);
	}

	/**
	 * Generate and present a form Assumes that an xsl file exists with the
	 * transform data in it.
	 */
	private void formgen(String form) throws IOException, ServiceException {
		try {
			usersession.log.debug("navcon:formgen:form:" + form);
			Document doc = usersession.startNewDoc();
			Element rootElem = usersession.doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = usersession.doc.createElement("global");
			rootElem.appendChild(globalElem);
			globalElem.setAttribute("CSSstylesheet", "styleOrig.css");
			globalElem.setAttribute("NodeKnownAs", usersession.genpropman.getProperty("NodeKnownAs").getValue());
			globalElem.setAttribute("LinkKnownAs", usersession.genpropman.getProperty("LinkKnownAs").getValue());
			globalElem.setAttribute("GraphKnownAs", usersession.genpropman.getProperty("GraphKnownAs").getValue());
			globalElem.setAttribute("visableMap", this.mapname);
			usersession.log.debug("navcon:formgen:before messagewords.");
			addAnyMessage2DOM(usersession.doc, rootElem );
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.transformout(doc, form, response);
			usersession.log.debug("navcon:formgen:end without exception.");
		} catch (Exception ex) {
			throw new ServiceException("navcon:formgen:ex:" + ex.toString() + usersession.stack2string(ex), ex);
		}
	} // formgen
	
	
	/*
	 * Given a file as param=URL, open file, new Unet2Net, get settingsNeturl from gotten Net, sendNormal
	 * sendUN differs using Net not neo4j and .unet is key: value, in an array 
	 */
	public void sendUN() throws ServiceException {
		URL myurl = null;
		usersession.log.debug("NC:sendUN():top.");
		try {			
			String identityhold = null;
			Net neta = null;
			net.nettree.Node startnode = null;
			unettree = null;
			Netprop settingneturl = null;
			String url = request.getParameter("URL");
			String startnodename = request.getParameter("startnodename");
			usersession.log.debug("NC:sendUN:url:" + url);
			usersession.startNewDoc();
			usersession.log.debug("NC:sendUN():in try:URL:" + url);
			// from here thru the copy and debug is bs and should be dropped ????
			myurl = new URL(url);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("cnet must not be blank.");
			} else {
				usersession.log.debug("NC:sendUN():not blank, myurl:" + myurl.toString());
			}
			File urlfile = new File("dummy");
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendUN():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendUN():urlfile.getName:" + urlfile.getName());
			usersession.log.debug("NC:sendUN():startnodename:" + startnodename);
			usersession.log.debug("NC:sendUN():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NC:sendUN():before createElement:root.");			
			Element rootElem = null;						
			try {
				usersession.log.debug("NC:sendUN():before new :Unet2Net: url:" + url);	
				netnav.Unet2Net unet2net = new netnav.Unet2Net(url, usersession);

				neta = unet2net.getNet();
				usersession.log.debug("NC:sendUN():neta.name:" + neta.name);
				usersession.log.debug("NC:sendUN:neta:n#:" + neta.getNodes().size() + " l#:" + neta.getLinks().size());	
				settingneturl = neta.getNetprop("settingNetUrl");
				usersession.log.debug("NC:sendUN():settingneturl.value:" + settingneturl.value);
				if ( usersession.settingsnet.getNetprop(file).value.equalsIgnoreCase(settingneturl.type) ) {
					usersession.log.debug("NC:sendUN():US.settingsnet file = settingneturl file.");	
				}
				usersession.log.debug("NC:sendUN():n#:" + unet2net.getNodeCount() + " l#:" + unet2net.getLinkCount());				
				if ( unet2net != null  ) {
					usersession.log.debug("NC:sendUN():unet2net != null. NOT NOT");
					neta = unet2net.getNet();
					usersession.log.debug("NC:sendUN():after new unet2net. Before new NetTree.");
					unettree = new net.nettree.NetTree(neta, neta.getNode(startnodename));
					if (this.unettree == null ) {
						usersession.log.debug("NC:sendUN:unettree == null. ");
					} else {
						usersession.log.debug("NC:sendUN:unettree != null. NOT NOT");
					}
				} else {
					usersession.log.debug("NC:sendUN():unet2net == null.");
					usersession.message4dom.messagewords.add("NC:sendUN:null Net returned null:");
				}
			} catch (NetTreeException nte ) {
				usersession.message4dom.messagewords.add("NC:sendUN:NetTreeException:" + nte.getMessage());
			}		
		} catch (ParserConfigurationException pce ) {
			usersession.log.debug("ParserConfigurationException:" + pce.toString());			
		} catch (IOException ioe ) {
			usersession.log.debug("IOException:" + ioe.toString());
		} finally {		
			usersession.log.debug("NC:sendUN():before sendNormalBegin.");
			sendNormalBegin(); 
		}
	} // sendUN
	
	/*
	 * Given a file as param=URL, open file, flush neographdbwork, call UnetIface, get NetTree, sendNormal
	 * sendUnet differes from sendJnet in that sendJnet is the old representation {key: value},
	 * and Unet is key: value, in an array 
	 */
	public void sendUnet() throws ServiceException {
		URL myurl = null;
		try {			
			String identityhold = null;
			neographworktx = null;
			nettreeA = null;
			String url = request.getParameter("URL");
			usersession.log.debug("NC:sendUnet:url:" + url);

			usersession.startNewDoc();
			usersession.log.debug("NC:sendUnet():in try:URL:" + URL);
			myurl = new URL(url);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("cnet must not be blank.");
			} else {
				usersession.log.debug("NC:sendUnet():myurl:" + myurl.toString());
			}
			File urlfile = new File("dummy");
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendUnet():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendUnet():urlfile.getName:" + urlfile.getName());
			usersession.log.debug("NC:sendUnet():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NC:sendUnet():in try.");
			usersession.log.debug("NC:sendUnet():before createElement:root.");			
			Element rootElem = null;						
			neographworktx = neographdbworking.beginTx();
			usersession.log
					.debug("NC:sendUnet():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.startNewDoc();
			try {
				UnetIface unetiface = new UnetIface(neographdbworking, urlfile, usersession);	
				if ( unetiface != null  ) {
					nettreeA = unetiface.getNetTree();					
				} else {
					usersession.message4dom.messagewords.add("unetiface returned null:");
				}
			} catch (NetTreeException nte ) {
				usersession.message4dom.messagewords.add("NetTreeException:" + nte.getMessage());
			}
			this.nodelist();
			if ( nettreeA != null ) {
				usersession.log.debug("NC:sendUnet:startnode from NT:getId:" + nettreeA.getStartNode().getId());
				try {
					if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
						usersession.log.debug("NC:sendUnet:startnode somehow equal 0");
					} else {
						usersession.log.debug("NC:sendUnet:startnode NOT NOT equal 0");
					}
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					usersession.log.debug("NC:sendUnet:nodeinlistId:" + nodeinlist.getId() + " nfe:" + nfe.toString());
					nettreeA.setStartNode(nodeinlist);			
				} catch ( NullPointerException npe ) {
					usersession.log.error("NC:sendUnet:nodeinlistId:" + nodeinlist.getId() + " npe:" + npe.toString());
					nettreeA.setStartNode(neographdbworking.getNodeById(nettreeA.getUnpresentedNode()) );
				}
				nettreeA.add2DOM(usersession.doc, rootElem, "");
				usersession.log.debug("NC:sendUnet:after a2d:available:" + neographdbworking.isAvailable(longer));	
			
			} else {
				usersession.log.error("NC:sendUnet:nettree == null.");
			}
		} catch (ParserConfigurationException pce ) {
			usersession.log.debug("ParserConfigurationException:" + pce.toString());
		} catch (IOException ioe ) {
			usersession.log.debug("IOException:" + ioe.toString());
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 
		}
	} //	sendUnet
	
	/*
	 * Given a file as param=URL, open file, flush neographdbwork, call jnetIface, get NetTree, sendNormal
	 * sendJnet differes from sendJson in that sendJson is the old representation key: ?, value: ?, type: ?
	 * and sendJnet is key: value pairs 
	 */
	public void sendJnet() throws ServiceException {
		URL myurl = null;
		try {			
			String identityhold = null;
			neographworktx = null;
			nettreeA = null;
			String url = request.getParameter("URL");
			usersession.log.debug("NC:sendJnet:url:" + url);

			usersession.startNewDoc();
			usersession.log.debug("NC:sendJnet():in try:URL:" + URL);
			myurl = new URL(url);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("cnet must not be blank.");
			} else {
				usersession.log.debug("NC:sendJnet():myurl:" + myurl.toString());
			}
			File urlfile = new File("dummy");
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendJnet():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendJnet():urlfile.getName:" + urlfile.getName());
			usersession.log.debug("NC:sendJnet():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NC:sendJnet():in try.");
			usersession.log.debug("NC:sendJnet():before createElement:root.");			
			Element rootElem = null;						
			neographworktx = neographdbworking.beginTx();
			usersession.log
					.debug("NC:sendJnet():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.startNewDoc();
			try {
				JnetIface jnetiface = new JnetIface(neographdbworking, urlfile, usersession);	
				if (jnetiface != null  ) {
					nettreeA = jnetiface.getNetTree();					
				} else {
					usersession.message4dom.messagewords.add("jnetiface returned null:");
				}
			} catch (NetTreeException nte ) {
				usersession.message4dom.messagewords.add("NetTreeException:" + nte.getMessage());
			}
			this.nodelist();
			if ( nettreeA != null ) {
				usersession.log.debug("NC:sendJnet:startnode from NT:getId:" + nettreeA.getStartNode().getId());
				try {
					if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
						usersession.log.debug("NC:sendJnet:startnode somehow equal 0");
					} else {
						usersession.log.debug("NC:sendJnet:startnode NOT NOT equal 0");
					}
				} catch (org.neo4j.graphdb.NotFoundException nfe) {
					usersession.log.debug("NC:sendJnet:nodeinlistId:" + nodeinlist.getId() + " nfe:" + nfe.toString());
					nettreeA.setStartNode(nodeinlist);			
				} catch ( NullPointerException npe ) {
					usersession.log.error("NC:sendJnet:nodeinlistId:" + nodeinlist.getId() + " npe:" + npe.toString());
					nettreeA.setStartNode(neographdbworking.getNodeById(nettreeA.getUnpresentedNode()) );
				}
				nettreeA.add2DOM(usersession.doc, rootElem, "");
				usersession.log.debug("NC:sendJnet:after a2d:available:" + neographdbworking.isAvailable(longer));	
			
			} else {
				usersession.log.error("NC:sendJnet:nettree == null.");
			}
		} catch (ParserConfigurationException pce ) {
			usersession.log.debug("ParserConfigurationException:" + pce.toString());
		} catch (IOException ioe ) {
			usersession.log.debug("IOException:" + ioe.toString());
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 
		}
	} // sendJnet	

	public void sendJson() throws ServiceException {
		URL myurl = null;
		try {			
			String identityhold = null;
			neographworktx = null;
			nettreeA = null;
			String url = request.getParameter("URL");
			usersession.log.debug("NC:SJ:url:" + url);

			usersession.startNewDoc();
			usersession.log.debug("NC:sendJson():in try:URL:" + URL);
			myurl = new URL(url);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("cnet must not be blank.");
			} else {
				usersession.log.debug("NC:sendJson():myurl:" + myurl.toString());
			}
			//String withoutslash = myurl.getFile().replaceAll("/", "");
			//File urlfile = new File(withoutslash.trim()); // 20200718 .trim
			File urlfile = new File("dummy");
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendJson():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendJson():urlfile.getName:" + urlfile.getName());
			usersession.log.debug("NC:sendJson():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NavCon:sendJson():in try.");
			usersession.log.debug("NavCon:sendJson():before createElement:root.");			
			Element rootElem = null;
			usersession.log.debug("NC:sendGedcom():before beginTx");			
			neographworktx = neographdbworking.beginTx();
			usersession.log
					.debug("NC:sendJson():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.startNewDoc();
			JsonIface jsoniface = new JsonIface(neographdbworking, urlfile, usersession);
			nettreeA = jsoniface.getNetTree();
			this.nodelist();
			if ( nettreeA != null ) {
				usersession.log.debug("NC:sendJson:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			} else {
				usersession.log.error("NC:sendJson:nettree == null.");
			}
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendJson:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendJson:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nfe) {
				usersession.log.debug("NC:sendJson:nodeinlistId:" + nodeinlist.getId() + " nfe:" + nfe.toString());
				nettreeA.setStartNode(nodeinlist);			
			} catch ( NullPointerException npe ) {
				usersession.log.error("NC:sendJson:nodeinlistId:" + nodeinlist.getId() + " npe:" + npe.toString());
				nettreeA.setStartNode(neographdbworking.getNodeById(nettreeA.getUnpresentedNode()) );
			}
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendJson:after a2d:available:" + neographdbworking.isAvailable(longer));	
		} catch (Exception ex) {
			usersession.log.error("NC:sendJson:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendjson:" + ":ex:" + ex.toString(), ex);
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 
		}
	} // sendjson
	
	/*
	 * Given a .unet json file in a param, it creates a new NetTree Net and puts it in 
	 * usersession.nettreeSettings. Copied from setNetTreeControl and neo replaced by n.n.Net
	 */
	public void setSettings() throws ServiceException {
		String identityhold = null;
		net.nettree.Net nethold = null;
		String uri = request.getParameter("uri");
		usersession.log.debug("NC:setSettings:top:uri:" + uri); 
		Element rootElem = null;
		try {
			URL myurl = new URL(uri);
			usersession.log.debug("NC:setSettings:Before Unet2Net:uri:" + uri );
			netnav.Unet2Net unet2net = new netnav.Unet2Net(uri, usersession);
			usersession.log.debug("NC:setSettings:Before getNet() -> usersession.netsettings");
			usersession.log.debug("NC:setSettings:unet2net.version:" + unet2net.version);
			usersession.log.debug("NC:setSettings:unet2net.linkcount:" + unet2net.getLinkCount());
			usersession.log.debug("NC:setSettings:US:netsettings.name:" + usersession.settingsnet.name);
			usersession.settingsnet = unet2net.getNet();	
			usersession.settingsnetset = true;
			usersession.log.debug("NC:setSettings:netprops.size:" + usersession.settingsnet.getNetProps().size());
			usersession.log.debug("NC:setSettings:nethold.nodes.size:"  + usersession.settingsnet.getNodes().size());
			usersession.log.debug("NC:setSettings:nethold.links.size:"  + usersession.settingsnet.getLinks().size());
			usersession.log.debug("NC:setSettings:bottom.");
			this.sendNormalBegin();
		} catch ( NullPointerException npe ) {
			usersession.log.error("NC:setSettings:npe:" + npe.toString());	
			throw new ServiceException(this.usersession, request, "NC:setSettings:" + ":npe:" + npe.toString(), npe);
		} catch (NetTreeException ex) {
			usersession.log.error("NC:setSettings:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NC:setSettings:" + ":ex:" + ex.toString(), ex);
		} catch (MalformedURLException mue ) {
			usersession.log.error("NC:setSettings:mue:" + mue.toString());
			throw new ServiceException(this.usersession, request, "NC:setSettings:" + ":ex:" + mue.toString(), mue);
		}
	} // setSettings

	/*
	 * Given a .cnet json file in a param, it calls controlIface and creates a new NetTreeControl and puts it in 
	 * usersession.nettreecontrol. controlIface uses neo4j
	 */
	public void setNetTreeControl() throws ServiceException {
		String identityhold = null;
		net.nettree.Net nethold = null;		
		usersession.log.debug("NC:SNTC:cnetfile:" + cnetfile); // cnetfile set in interpreter
		Element rootElem = null;
		try {
			URL myurl = new URL(cnetfile);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("cnet must not be blank.");
			} else {
				usersession.log.debug("NC:SNTC:URL.toString:" + myurl.toString());
			}			
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:SNTC:Before new controliface." );
			ControlIface controliface = new ControlIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:SNTC:Before getNetControl.");
			if (cnetfile.endsWith("default.cnet")) {	
				//messenger("NC:SNT:", "NC:SNT:cnetfile:" + cnetfile);
				usersession.nettreecontrol = new NetTreeControl(usersession.log, controliface.getControlNet(),
						usersession, "LineControl" );
			} else {
				usersession.nettreecontrol = new NetTreeControl(usersession.log, controliface.getControlNet(),
						usersession, "LineControl" );
			}
			usersession.log.debug("NC:SNTC:After getNetControl." );
			usersession.log.debug("NC:SNTC:linespechash size:" + usersession.nettreecontrol.linespecsethash.size() );
			//messenger("NC:SNTC:", "Control Net set to:\"" + usersession.nettreecontrol.NTNet.name + "\"" );
			usersession.settingsnetset = false;
			this.sendNormalBegin();
		} catch ( NullPointerException npe ) {
			usersession.log.error("NC:SNTC:npe:" + npe.toString());	
			throw new ServiceException(this.usersession, request, "NC:SCN:" + ":npe:" + npe.toString(), npe);
		} catch (Exception ex) {
			usersession.log.error("NC:SNTC:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NC:SCN:" + ":ex:" + ex.toString(), ex);
		}
		//sendNormalBegin();
	} // setNetTreeControl
	
	/*
	 * sendGedcom genelogocal exchange relies on a query string with
	 * URL=<some url> where a gexf file is located URL is a string in the request
	 * e.g. localhost//patent.gexf 
	 * The neo4j scratch db was opened in the NavCon
	 * constructor, a transaction is begun, db data is deleted, document started,
	 * new GexfIface created pointing to the URL, a NetTree is gotten from
	 * GexfIface, and the doc is populated with the nettree lines.
	 */
	public void sendGedcom() throws ServiceException {
		usersession.log.debug("NC:sendGedcom():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;
		URL myurl = null;
		/* templetURL set in interperter 
		//templetURL = request.getParameter("templet");
		if ( templetURL != null && !templetURL.trim().isEmpty()  ) { // NOT 
			usersession.log.debug("NC:sendGedcom:templetURL:" + templetURL.toString());
			usersession.genpropman.openConnectionLoadTempletProps(templetURL);
			usersession.resetLineDisplaySys();
		} // if			
	
		netpropsURL = request.getParameter("netpropsURL");
		if ( netpropsURL != null && netpropsURL.trim().length() > 0 ) { 
			usersession.log.debug("NC:sendGedcom:netpropsURL not empty:" + netpropsURL.toString());
			usersession.genpropman.openConnectionLoadNetworkProps(netpropsURL);
		}
			*/	

		Element rootElem = null;
		try {
			usersession.startNewDoc();
			usersession.log.debug("NC:sendGedcom():in try:URL:" + URL);
			myurl = new URL(URL);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendGedcom():myurl:" + myurl.toString());
			}
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			usersession.log.debug("NC:sendGedcom():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGedcom():urlfile:canWrite():" + urlfile.canWrite());
			// String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			usersession.log.debug("NC:sendGedcom():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NC:sendGedcom():before append:root.");
			usersession.doc.appendChild(rootElem);
			usersession.log.debug("NC:sendGedcom():before beginTx");
			
			neographworktx = neographdbworking.beginTx();
			usersession.log
					.debug("NC:sendGedcom():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendGedcom():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendGedcom():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGedcom():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendGedcom():after U2F: before new GedcomIface:urlfile:" + urlfile.getName());
			GedcomIface gedcomiface = new GedcomIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:sendGedcom:after new Gedcomiface. before getNetTree. GIF ver:" + gedcomiface.version);			
			nettreeA = gedcomiface.getNetTree();
			//messenger("NC:SGedcom:", "Gedcom:" + gedcomiface.getNodeCount() + "," + gedcomiface.getLinkCount() );			
			usersession.log.debug("NC:sendGedcom:after getNetTree:totalEdgeCount:" + nettreeA.totalEdgeCount
					+ " nodes:" + nettreeA.getNodeIsPresentedSize());
			usersession.log.debug("NC:sendGedcom:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log.debug("NC:sendGedcom:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGedcom:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGedcom:before a2d:nodecount:" + nodecount());
			this.nodelist();
			usersession.log.debug("NC:sendGedcom:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendGedcom:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendGedcom:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendGedcom:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendGedcom:after a2d:available:" + neographdbworking.isAvailable(longer));
			//usersession.transformout(usersession.doc, "map.xsl", usersession.response);
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendGedcom:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.debug("NC:sendGedcom:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendGedcom:" + ":ex:" + ex.toString(), ex);
		} finally {
			//usersession.reset`Properties();			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 	
		}
	} // sendGedcom
	
	public void sendGexf() throws ServiceException {
		usersession.log.debug("NC:sendGexf():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;
		// URL set in interperter .getParam("URL")
		URL myurl = null;
		/*templetURL = request.getParameter("templet");
		usersession.genpropman.openConnectionLoadTempletProps(templetURL);
		usersession.resetLineDisplaySys();
		*/
		Element rootElem = null;
		try {
			usersession.startNewDoc();
			usersession.log.debug("NC:sendGexf():in try:URL:" + URL);
			myurl = new URL(URL);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendGexf():myurl:" + myurl.toString());
			}
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			usersession.log.debug("NC:sendGexf():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGexf():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NavCon:sendGexf():in try.");
			usersession.log.debug("NavCon:sendGexf():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NavCon:sendGexf():before append:root.");
			usersession.doc.appendChild(rootElem);
			usersession.log.debug("NC:sendGexf():before beginTx");
			neographworktx = neographdbworking.beginTx();	

			usersession.log.debug("NC:sendGexf():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendGexf():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendGexf():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGexf():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendGexf():after U2F: before new GexfIface:urlfile:" + urlfile.getName());

			GexfIface gexfiface = new GexfIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:sendGexf:after new gexfiface. before getNetTree. GIF ver:" + gexfiface.version);
			
			nettreeA = gexfiface.getNetTree();

			usersession.log.debug("NC:sendGexf:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log
					.debug("NC:sendGexf:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGexf:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGexf:before a2d:nodecount:" + nodecount());
			this.nodelist();
			if ( nettreeA != null ) {
				usersession.log.debug("NC:sendGexf:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			} else {
				usersession.log.error("NC:sendGexf:nettree == null.");
			}
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendGexf:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendGexf:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendGexf:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);			
			} catch ( NullPointerException npe ) {
				usersession.log.debug("NC:sendGexf:nodeinlistId:" + nodeinlist.getId() + " npe:" + npe.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			// nodecount());
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendGexf:after a2d:available:" + neographdbworking.isAvailable(longer));

		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendGexf:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.error("NC:sendGexf:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendGexf:" + ":ex:" + ex.toString(), ex);
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 
		}
	} // sendGexf

	/*
	 * sendGexf (graph exchange format .gexf) relies on a <name>.net file for its inputs
	 */
	public void sendGexfNEW() throws ServiceException {
		usersession.log.debug("NC:sendGexf():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;		
		URL myurl = null;
		/*templetURL = request.getParameter("templet");  templetURL set in interperte 
		usersession.genpropman.openConnectionLoadTempletProps(templetURL); // ?? 20190811
		usersession.resetLineDisplaySys();
		*/
		Element rootElem = null;
		try {
			usersession.startNewDoc();
			usersession.log.debug("NC:sendGexf():in try:URL:" + URL);
			myurl = new URL(URL); // URL set in interperter .getParam("URL")
			if (myurl.toString().trim().equals("")) {
				messenger("NC:sendGexf", "URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendGexf():myurl:" + myurl.toString());
			}
			//File urlfile = new File("holder");
			// String withoutslash = myurl.getFile().replaceAll("/", "");
			// File urlfile = new File(withoutslash);
			File urlfile = new File("dummy");
			usersession.log.debug("NC:sendGexf():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGexf():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NavCon:sendGexf():in try.");
			//String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			usersession.log.debug("NavCon:sendGexf():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NavCon:sendGexf():before append:root.");
			usersession.doc.appendChild(rootElem);
			
			usersession.log.debug("NC:sendGexf():before beginTx");
			neographworktx = neographdbworking.beginTx();	
			usersession.log.debug("NC:sendGexf():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendGexf():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendGexf():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGexf():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendGexf():after U2F: before new GexfIface:urlfile:" + urlfile.getName());

			GexfIface gexfiface = new GexfIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:sendGexf:after new gexfiface. before getNetTree. GIF ver:" + gexfiface.version);

			gexfiface.setGraphProp("NC:sendGexf Before getNetTree: Version", version);
			
			nettreeA = gexfiface.getNetTree();
			//messenger("NC:SGexf:", "Gexf:" + gexfiface.getNodeCount() + "," + gexfiface.getLinkCount() );
			
			usersession.log.debug("NC:sendGexf:after getNetTree:totalEdgeCount:" + nettreeA.totalEdgeCount
					+ " nodes:" + nettreeA.getNodeIsPresentedSize());
			usersession.log.debug("NC:sendGexf:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log
					.debug("NC:sendGexf:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGexf:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGexf:before a2d:nodecount:" + nodecount());
			this.nodelist();
			if ( nettreeA != null ) {
				usersession.log.debug("NC:sendGexf:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			} else {
				usersession.log.error("NC:sendGexf:nettree == null.");
			}
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendGexf:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendGexf:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendGexf:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			// nodecount());
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendGexf:after a2d:available:" + neographdbworking.isAvailable(longer));
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendGexf:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.error("NC:sendGexf:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendGexf:" + ":ex:" + ex.toString(), ex);
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin(); 
		}
	} // sendGexf
	

	/*
	 *  front end to what previously send(iface) 
	 *  The netfileurl parameter points to a properties file (URL)  
	 *  That properties file contains:
	 *  key=interface value=<iface> to know what interface to use to convert the file to the scratch graph database
	 *  key=netfile value=<a file that contains the URL of the file contain a graph/network description
	 *  key=netprops value=<a properties file that contains the NetProps of the Network>
	 *  key=extraprops value=<a properties file that contains additional properties>  

	public void openNetFile() throws ServiceException {
		String netFileAsURL = request.getParameter("netfileurl");
		String key = "";
		String value = "";
		//messenger("NC:ONF", "netfileurl:" + netFileAsURL.toString());
		if (netFileAsURL.trim().equals("")) {
			messenger("NC:ONF:", "The network spec URL must be specified.");
		} else {
			genpropman.loadPropsFromFile(genpropman.netSpecProps, netFileAsURL);

			if (genpropman.netSpecProps.containsKey("netprops")) {
				genpropman.loadPropsFromFile(genpropman.netProps,
						genpropman.netSpecProps.get("netprops").toString());
				if (genpropman.netSpecProps.containsKey("interface")) {
					this.templetURL = genpropman.netSpecProps.get("interface").toString();
					if (genpropman.netSpecProps.containsKey("netfile")) {
						this.URL = genpropman.netSpecProps.getProperty("netfile");
						messenger("NC:ONF:", "OK so far:I/U/N:" + templetURL + "/" + URL + "/" 
								+ genpropman.netSpecProps.get("netprops").toString());
						if (templetURL.equalsIgnoreCase("graphviz")) {
							messenger("NC:ONF:", "Before call to sendGV");
							this.sendGV();
						} else if (templetURL.equalsIgnoreCase("gexf")) {
							messenger("NC:ONF:", "Before call to sendGexf");
							this.sendGexf();							
						} else {
							messenger("NC:ONF:", "NetInterface: not available.");
						}
					} else {
						messenger("NC:ONF:", "Network spec must contain the input file location.");
					}
				} else {
					messenger("NC:ONF:", "Network spec must contain an interface key.");
				}
			} else {
				messenger("NC:ONF:", "Network spec must contain a netprops key.");
			}
		}
		//sendNormalBegin();
	}
	*/

	/*
	 * sendGV (graphviz, dot) relies on a  URL=<*.net> a properties file containing the URL of the input file
	 * the URL of the properties file and the interface specifier. 
	 */
	public void sendGV() throws ServiceException {
		usersession.log.debug("NC:sendGV():top.");
		neographworktx = null;
		nettreeA = null;
		URL myurl = null;
		/*templetURL = request.getParameter("templet");  // 20190807 testing opennet
		//usersession.genpropman.openConnectionLoadTempletProps(templetURL);
		usersession.resetLineDisplaySys();
		*/
		Element rootElem = null;
		try {
			usersession.startNewDoc();
			//usersession.log.debug("NC:sendGV():in try:URL:" + URL);
			myurl = new URL(URL);  // URL was an getParameter now set by openNetFile
			if (myurl.toString().trim().equals("")) {
				messenger("NC:SGV", "URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendGV():myurl:" + myurl.toString());
			}
			rootElem = usersession.doc.createElement("root");
			usersession.doc.appendChild(rootElem);
			//usersession.log.debug("NC:sendGV():before beginTx");
			neographworktx = neographdbworking.beginTx();
			usersession.log.debug("NC:sendGV():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendGV():nodecount:" + nodecount() + " after deleteAll(db)");
			File urlfile = new File("dummy");
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendGV():after copyU2F: before new GexfIface:urlfile:" + urlfile.getName());
			GVIface gviface = new GVIface(neographdbworking, urlfile, usersession);
			usersession.log
					.debug("NC:sendGV:after new GVIface. before getNetTree. GIF ver:" + gviface.version);
			//gviface.setGraphProp("NC:sendGV Before getNetTree: Version", version);
			nettreeA = gviface.getNetTree();
			//messenger("NC:SGV", "GV:" + gviface.getNodeCount() + "," + gviface.getLinkCount());
			usersession.log.debug("NC:sendGV:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendGV:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendGV:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.error("NC:sendGV:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			// addAnyMessage2DOM(usersession.doc, rootElem );
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendGV:after a2d:available:" + neographdbworking.isAvailable(longer));			
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendGV:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.debug("NC:sendGV:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendGV:" + ":ex:" + ex.toString(), ex);
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();  		
		} 
	} // sendGV()	
	
	/*
	 * sendER connects with schemacrawler to produce an Entity Relationship diagram
	 * URL=<some url> which is a JDBC connection string
	 * The neo4j scratch db was opened in the NavCon
	 * constructor, a transaction is begun, db data is deleted, document started,
	 * new ERIface created pointing to the connection string in future, a NetTree is gotten from
	 * ER Iface, and the doc is populated with the nettree lines.
	 */
	public void sendER() throws ServiceException {
		usersession.log.debug("NC:sendER():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;
		URL myurl = null;			
		Element rootElem = null;
		try {
			usersession.startNewDoc();			
			usersession.log.debug("NC:sendER():in try:URL:" + URL);
			myurl = new URL(URL);
			/*templetURL = request.getParameter("templet");
			usersession.genpropman.openConnectionLoadTempletProps(templetURL);
			usersession.resetLineDisplaySys();
			*/
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendER():myurl:" + myurl.toString());
			}
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			usersession.log.debug("NC:sendER():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendER():urlfile:canWrite():" + urlfile.canWrite());
			usersession.log.debug("NavCon:sendGV():in try.");
			//String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");			
			usersession.log.debug("NavCon:sendER():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NavCon:sendER():before append:root.");
			usersession.doc.appendChild(rootElem);
			usersession.log.debug("NC:sendER():before beginTx");
			neographworktx = neographdbworking.beginTx();

			usersession.log.debug("NC:sendER():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendER():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendER():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendER():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendER():after U2F: before new ERIface:urlfile:" + urlfile.getName());

			ERIface eriface = new ERIface(neographdbworking, urlfile, usersession);
			usersession.log
					.debug("NC:sendER:after new ERIface. before getNetTree. ERIFace ver:" + eriface.version);
			
			nettreeA = eriface.getNetTree();
			//messenger("NC:SER:", "ER" + eriface.getNodeCount() + "," + eriface.getLinkCount() );
			
			usersession.log.debug("NC:sendER:after getNetTree:totalEdgeCount:" + nettreeA.totalEdgeCount
					+ " nodes:" + nettreeA.getNodeIsPresentedSize());
			usersession.log.debug("NC:sendGV:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log.debug("NC:sendER:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendER:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendER:before a2d:nodecount:" + nodecount());
			//this.nodelist();
			usersession.log.debug("NC:sendER:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendER:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendER:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendER:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendER:after a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.transformout(usersession.doc, "map.xsl", usersession.response);
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendER:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.debug("NC:sendER:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendER:" + ":ex:" + ex.toString(), ex);
		} finally {			
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		}
	}	// sendER
	
	public void sendListAdjacentEdges() throws ServiceException {
		ArrayList<GenProp> displaynodeProps = new ArrayList<>();
		ArrayList<GenProp> linkrefProps = new ArrayList<>();
		try {
			// this.sendNormalBegin();
			Document doc = usersession.startNewDoc();
			usersession.transformout(doc, "menutop.xsl", response);
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			//String target = "listadjacentedges.xsl";
			String target = "actionpop.xsl";
			globalElem.setAttribute("mode", "normal");
			globalElem.setAttribute("target", target);
			globalElem.setAttribute("CSSstylesheet", usersession.CSSstylesheet);
			globalElem.setAttribute("NodeKnownAs", usersession.genpropman.getProperty("NodeKnownAs").getValue());
			globalElem.setAttribute("LinkKnownAs", usersession.genpropman.getProperty("LinkKnownAs").getValue());
			globalElem.setAttribute("GraphKnownAs", usersession.genpropman.getProperty("GraphKnownAs").getValue());
			try {
				neographworktx = neographdbworking.beginTx();				
				Long Linternalid = java.lang.Long.valueOf(internalid); // internalid from request.getParam
				neonodework = neographdbworking.getNodeById(Linternalid);
				Iterator<Relationship> neorelationshipiter = neonodework.getRelationships().iterator();
				while (neorelationshipiter.hasNext() ) {
					neorelationshipwork = neorelationshipiter.next();	
					usersession.log.debug("NavCon:sendListAdjacentEdges:endNodeid:" + neorelationshipwork.getEndNode().getId() );
					usersession.log.debug("NavCon:sendListAdjacentEdges:startNodeid:" + neorelationshipwork.getStartNode().getId() );
				}				
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NavCon:sendListAdjacentEdges:graphdb.NotFoundException:" + nnf.toString());
			} catch (NullPointerException npe) {
				usersession.log.debug("NavCon:sendListAdjacentEdges:NullPointer.");
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				usersession.log.debug("NavCon:sendListAdjacentEdges:ArrayIndexOutOfBoundsException.");
			} finally {
				usersession.log.debug("NavCon:sendListAdjacentEdges:before add2line DOM");
			}
			usersession.transformout(doc, target, response);
			// this.sendNormalEnd();
		} catch (Exception ex) {
			usersession.log.debug("NavCon:sendListAdjacentEdges:e:" + ex.toString());
			throw new ServiceException(this.usersession, request, "NavCon:actionpop:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
		}
	} // sendListAdjacentEdges
	
	/*
	 * sendDot (Graphviz format) relies on a query string with URL=<some url> where
	 * a dot file is located URL is a string in the request e.g. localhost//unix.gv
	 * The neo4j scratch db was opened in the NavCon constructor, a transaction is
	 * begun, db data is deleted, document started, new DotIface created pointing to
	 * the URL, a NetTree is gotten from DotIface, and the doc is populated with the
	 * nettree lines.
	 */
	public void sendDot() throws ServiceException {
		usersession.log.debug("NC:sendDot():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;
		URL myurl = null;
		sendNormalBegin();
		Element rootElem = null;
		try {
			usersession.startNewDoc();
			usersession.log.debug("NC:sendDot():in try:URL:" + URL);
			myurl = new URL(URL);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendDot():myurl:" + myurl.toString());
			}
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			usersession.log.debug("NC:sendDot():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendDot():urlfile:canWrite():" + urlfile.canWrite());
			//String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			// Element rootElem = doc.createElement("WordGraphXL"); // this worked in
			// netbeans with edgeTree
			usersession.log.debug("NavCon:sendDot():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NavCon:sendDot():before append:root.");
			usersession.doc.appendChild(rootElem);
			usersession.log.debug("NC:sendDot():before beginTx");
			neographworktx = neographdbworking.beginTx();

			usersession.log
					.debug("NC:sendDot():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendDot():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendDot():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendDot():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendDot():after U2F: before new GexfIface:urlfile:" + urlfile.getName());

			DotIface dotiface = new DotIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:sendDot: DIF ver:" + dotiface.version);
			// dotiface.setGraphProp("NavCon:Version:", version);
			usersession.log.debug("NC:sendDot: Before getNetTree.");
			
			nettreeA = dotiface.getNetTree();
			//messenger("NC:SDot", "Dot:" + dotiface.getNodeCount() + "," + dotiface.getLinkCount() );
			
			usersession.log.debug("NC:sendDot:after getNetTree:totalEdgeCount:" + nettreeA.totalEdgeCount
					+ " nodes:" + nettreeA.getNodeIsPresentedSize());
			usersession.log.debug("NC:sendDot:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log
					.debug("NC:sendDot:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendDot:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendDot:before a2d:nodecount:" + nodecount());
			this.nodelist();
			usersession.log.debug("NC:sendDot:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendDot:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendDot:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendDot:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			// usersession.log.debug("NC:sendDot:right before a2d:nodecount:" +
			// nodecount());
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendDot:after a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.transformout(usersession.doc, "map.xsl", usersession.response);
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendDot:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			// this.usersession.message4dom.add2DOM(usersession.doc, rootElem,"");
			// usersession.transformout(usersession.doc, "map.xsl", response);
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.debug("NC:sendDot:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendDot:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} // SendDOT
	} // SendDOT

	public void sendGML() throws ServiceException {
		usersession.log.debug("NC:sendDot():top.");
		String identityhold = null;
		neographworktx = null;
		nettreeA = null;
		URL myurl = null;
		sendNormalBegin();
		Element rootElem = null;
		try {
			usersession.startNewDoc();
			usersession.log.debug("NC:sendGML():in try:URL:" + URL);
			myurl = new URL(URL);
			if (myurl.toString().trim().equals("")) {
				usersession.addtomessage("URL must not be blank.");
			} else {
				usersession.log.debug("NC:sendGML():myurl:" + myurl.toString());
			}
			String withoutslash = myurl.getFile().replaceAll("/", "");
			File urlfile = new File(withoutslash);
			usersession.log.debug("NC:sendGML():myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGML():urlfile:canWrite():" + urlfile.canWrite());
			//String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			// Element rootElem = doc.createElement("WordGraphXL"); // this worked in
			// netbeans with edgeTree
			usersession.log.debug("NC:sendGML():before createElement:root.");
			rootElem = usersession.doc.createElement("root");
			usersession.log.debug("NC:sendGML():before append:root.");
			usersession.doc.appendChild(rootElem);
			usersession.log.debug("NC:sendGML():before beginTx");
			neographworktx = neographdbworking.beginTx();

			usersession.log
					.debug("NC:sendGML():after beginTx():nodecount:" + nodecount() + " before deleteAll(db)");
			deleteAll(neographdbworking);
			usersession.log.debug("NC:sendGML():nodecount:" + nodecount() + " after deleteAll(db)");
			usersession.log.debug("NC:sendGML():before copyURLToFile:myurl:" + myurl.toString());
			usersession.log.debug("NC:sendGML():urlfile:canWrite():" + urlfile.canWrite());
			FileUtils.copyURLToFile(myurl, urlfile); // from, to
			usersession.log.debug("NC:sendGML():after U2F: before new GexfIface:urlfile:" + urlfile.getName());

			GMLIface gmliface = new GMLIface(neographdbworking, urlfile, usersession);
			usersession.log.debug("NC:sendGML: DIF ver:" + gmliface.version);
			// gmliface.setGraphProp("NavCon:Version:", version);
			usersession.log.debug("NC:sendGML: Before getNetTree.");
			
			nettreeA = gmliface.getNetTree();
			//messenger("NC:SDot", "Dot:" + gmliface.getNodeCount() + "," + gmliface.getLinkCount() );
			
			usersession.log.debug("NC:sendGML:after getNetTree:totalEdgeCount:" + nettreeA.totalEdgeCount
					+ " nodes:" + nettreeA.getNodeIsPresentedSize());
			usersession.log.debug("NC:sendGML:isAvailable:" + neographdbworking.isAvailable(longer));

			usersession.log
					.debug("NC:sendGML:after getNetTree:isAvailable:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGML:before a2d:available:" + neographdbworking.isAvailable(longer));
			usersession.log.debug("NC:sendGML:before a2d:nodecount:" + nodecount());
			this.nodelist();
			//usersession.log.debug("NC:sendGML:startnode from NT:getId:" + nettreeA.getStartNode().getId());
			try {
				if (neographdbworking.getNodeById(nettreeA.getStartNode().getId()).equals(Long.valueOf("0"))) {
					usersession.log.debug("NC:sendGML:startnode somehow equal 0");
				} else {
					usersession.log.debug("NC:sendGML:startnode NOT NOT equal 0");
				}
			} catch (org.neo4j.graphdb.NotFoundException nnf) {
				usersession.log.debug("NC:sendGML:nodeinlistId:" + nodeinlist.getId() + " nnf:" + nnf.toString());
				nettreeA.setStartNode(nodeinlist);
			}
			// usersession.log.debug("NC:sendGML:right before a2d:nodecount:" +
			// nodecount());
			nettreeA.add2DOM(usersession.doc, rootElem, "");
			usersession.log.debug("NC:sendGML:after a2d:available:" + neographdbworking.isAvailable(longer));
			//usersession.transformout(usersession.doc, "map.xsl", usersession.response); // ???? 20191102
		} catch (java.io.FileNotFoundException fnfex) {
			usersession.log.debug("NC:sendGML:ex:" + fnfex.toString());
			usersession.addtomessage("File not found at " + myurl.toString());
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} catch (Exception ex) {
			usersession.log.error("NC:sendGML:ex:" + ex.toString());
			throw new ServiceException(this.usersession, request, "sendDot:" + ":ex:" + ex.toString(), ex);
		} finally {
			neographworktx.success();
			neographworktx.close();
			sendNormalBegin();
		} // SendGML
	} // SendGML
	
	public void sendNeo4j() throws ServiceException {
		usersession.log.debug("NC:sendNeo4j():top.");
		String dblocation = request.getParameter("dblocation");
		String identityhold = null;
		NetTree nettreeA = null;
		try {
			sendNormalBegin();
			usersession.log.debug("NavCon:sendNeo4j():in try. version:" + version);
			//String serverfilepath = usersession.httpsessionA.getServletContext().getRealPath("/");
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			usersession.log.debug("NC:sendNeo4j():before Neo4jIface");
			if (dblocation == null) {
				usersession.log.error("NC:sendNeo4j():dblocation == null.");
				messenger("NC:sendNeo4j", "dblocation == null.");
			}
			Neo4jIface neo4jiface = new Neo4jIface(usersession.out, dblocation, usersession);

			usersession.log.debug("NC:sendNeo4j:after new neo4jiface. before getNetTree.");

			nettreeA = neo4jiface.getNetTree();
			
			usersession.log.debug("NC:sendNeo4j:after getNetTree.");
			usersession.log.debug("NC:sendNeo4j:before add2DOM nettree string:" + nettreeA.toString());
			nettreeA.add2DOM(doc, rootElem, "");
			usersession.log.debug("NC:sendNeo4j:after a2d.");
			String target = "map.xsl";
			usersession.transformout(doc, target, response);
		} catch (Exception ex) {
			usersession.log.debug("NC:sendNeo4j:ex:" + ex.toString());
			// ex.printStackTrace(usersession.out);
			throw new ServiceException(this.usersession, request, "NavCon:sendGraphXML:" + ":ex:" + ex.toString(), ex);
		}
	} // sendNeo4j
	
	/**
	 * ToDo: see if there is a faster way to delete a database
	 * @param neographdbI
	 */
	private void deleteAll(GraphDatabaseService neographdbI) {
		usersession.log.debug("NC:deleteAll:top:neographdbI:" + neographdbI.toString());
		int nodecount = 0;
		Map<String, Object> neoprops = new HashMap<>();
		org.neo4j.graphdb.Node neonodehold = null;
		Attribute attrhold = null;
		ResourceIterator<org.neo4j.graphdb.Node> neoallNodes = neographdbI.getAllNodes().iterator();
		try {
			while (neoallNodes.hasNext()) {			
				neonodehold = neoallNodes.next();
				//usersession.log.debug("NC:deleteAll:in iterator:nodehode.getid:" + neonodehold.getId());
				deleteNode(neonodehold);
				nodecount++;
			}
		} catch (Exception e ) {
			messenger("NC:DA", "exception:e:" + e.toString());
			usersession.log.error("NC:deleteAll:exception:e:" + e.toString()); 
		}
		usersession.log.debug("NC:deleteAll:count:" + nodecount);
	}

	private void deleteNode(org.neo4j.graphdb.Node neonodeI) {
		try {
			Iterable<Relationship> allRelationships = neonodeI.getRelationships();
			for (Relationship relationship : allRelationships) {
				//usersession.log.debug("NC:DN:delete relaionship:" + relationship.toString());
				relationship.delete();
			}
			neonodeI.delete();
		} catch (Exception e ) {
			messenger("NC:DN", "exception:e:" + e.toString());
			usersession.log.error("NC:DN:exception:e:" + e.toString()); 
		}
	}

	private int nodecount() {
		org.neo4j.graphdb.Node nodenot = null;
		ResourceIterator<org.neo4j.graphdb.Node> allnodeiter = neographdbworking.getAllNodes().iterator();
		int nodecounter = 0;
		while (allnodeiter.hasNext()) {
			nodenot = allnodeiter.next();
			nodecounter++;
		}
		return nodecounter;
	}

	private void nodelist() {
		Boolean first = true;
		org.neo4j.graphdb.Node nodenot = null;
		ResourceIterator<org.neo4j.graphdb.Node> allnodeiter = neographdbworking.getAllNodes().iterator();
		int nodecounter = 0;
		while (allnodeiter.hasNext()) {
			nodenot = allnodeiter.next();
			if (first) {
				nodeinlist = nodenot;
				first = false;
				usersession.log.debug("NC:Nodelist:first node in list id:" + nodeinlist.getId());
			}
			// usersession.log.debug("NC:Nodelist:num:" + nodecounter + " Id:" + nodenot.getId());
			nodecounter++;
		}
	}
	
	public void shownlevel() throws ServiceException {
		if ( fx1.equalsIgnoreCase("-1") ) {
			usersession.maxlevel = usersession.maxlevel-1;
			this.contract2Level();
		} else if ( fx1.equalsIgnoreCase("+1") ) {
			usersession.maxlevel = usersession.maxlevel+1;
			this.contract2Level();
		}
	}

	public String getVersion() {
		return version;
	}
	
	public void resetLineDisplaySys() throws ServiceException {
		usersession.resetLineDisplaySys();
		this.sendNormalBegin();
	}

	private static File stream2file(InputStream in) throws IOException {
		final File tempFile = File.createTempFile("stream2file", ".tmp");
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public void menugen() throws IOException, ServiceException {
		String menu = request.getParameter("menu");
		menugen(menu);
	}

	/**
	 * Generate and present a menu Assumes that an xsl file exists with the
	 * transform data in it.
	 */
	public void menugen(String menu) throws IOException, ServiceException {
		try {
			usersession.log.debug("go:menugen:menu:" + menu);
			Menu menuA = new Menu(menu);
			Document doc = usersession.startNewDoc();
			Element rootElem = doc.createElement("root");
			doc.appendChild(rootElem);
			Element globalElem = doc.createElement("global");
			rootElem.appendChild(globalElem);
			usersession.log.debug("go:menugen:before setAttributes.");
			if ( usersession.genpropman != null ) {
				globalElem.setAttribute("CSSstylesheet", "styleOrig.css");
				globalElem.setAttribute("NodeKnownAs", usersession.genpropman.getProperty("NodeKnownAs").getValue());
				globalElem.setAttribute("LinkKnownAs", usersession.genpropman.getProperty("LinkKnownAs").getValue());
				globalElem.setAttribute("GraphKnownAs", usersession.genpropman.getProperty("GraphKnownAs").getValue());
			}
			usersession.log.debug("go:menugen:after setAttributes.");
			addAnyMessage2DOM(usersession.doc, rootElem );
			usersession.transformout(doc, "menutop.xsl", response);
			usersession.log.debug("go:navcon:menugen:before add2DOM.");
			menuA.add2DOM(doc, rootElem, "");
			usersession.transformout(doc, menu, response);
		} catch (Exception ex) {
			throw new ServiceException("go:navcon:menugen:ex:" + ex.toString());
		}
	} // menugen
	
	private void messenger(String wherefrom, String message ) {
		usersession.message4dom.messagewords.add(message);
		usersession.log.debug("Msg:" + wherefrom + ":" + message  );		
	}
	
	private void addAnyMessage2DOM(Document docI, Element rootElemI ) {
		//messenger("AAM2D", "mSize:" + usersession.message4dom.messagewords.size() );
		Document doc = docI;
		Element rootElem = rootElemI;
		if (usersession.message4dom.messagewords.size() > 0) {
			usersession.message4dom.add2DOM(doc, rootElem, ""); // default name:message
			// usersession.message4dom.messagewords.clear();
		}	
	}

} // NavCon

