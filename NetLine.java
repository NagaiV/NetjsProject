
package netnav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.ListIterator;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.io.Serializable;
import java.lang.reflect.Field;

import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.jfree.util.Log;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.PropertyContainer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.nettree.Link;
import net.nettree.Net;
import net.nettree.NetTreeException;
import net.nettree.Nprop;

/**
 * 
 * @author Robert Garvey, Copyright 1998-2018 All rights reserved
 * 
 *         NetLines are produced by NetTree and referenced in ArrayList
 *         lineslist that when created properly positions the NetLines in the
 *         ArrayList such that the order is correct when iterated over Each
 *         NetLine carries the information that is used for display on the line
 *         and navigation information to build servlet requests to navigate the
 *         system. NetLine information makes its way out via Add2DOM as the
 *         current system is built on xsl transforms.
 * 
 *         version 2b 1998 April version 3b January 2000
 * @version 4a 2000-09-27 Added updatable components. 2003-09-11 1018 Add2DOM
 *          20180115 1744 rbg Try in Gephi dropped Vector lineObjV from
 *          constructor 20180223 1920 rbg Working on NetTree 20180508 Need to
 *          figure the return of display node. Probably should go to properties
 *          and get name unless there is a name for neo 20180710 internal id if
 *          0 set to string 0. 20181117 1552 get link ref and linkinternal id
 *          straight. 20190519 get link ref and linkinternal id straight good
 *          a2d. 20190723 working on getting the link info dsplayed in the line
 *          ┌ << (F) -> (D) fd:ad 20191216 working on properties and ┌ << (F) ->
 *          (D) fd:ad 20200121 working on LineDisplaySys to get the lines
 *          formatted "20200227 this.type.equalsIgnoreCase(focus)" "20200326
 *          1245 rbg implementing ControNet linecontrol" 202000421 1017 rbg
 *          implementing NetTreeControl with line entries.
 */

public class NetLine implements Serializable {

	private static final long serialVersionUID = 1L;
	public String version = "20200503 1141 rbg implementing NetTreeControl with line entries.";

	/**
	 * Identification of the line, with additions and deletions the vector index can
	 * not be relied upon. It is used as a uniqueid.
	 */
	private int lineno;

	/**
	 * Reference to the neo4j node that is typically displayed first on the line
	 */
	public Node displaynode;

	/**
	 * Reference to the link represented by the line in neo4j it is called a
	 * Relationship
	 */
	public Relationship linkref;

	/**
	 * "hypo", "hyper", "focus"
	 */
	public String type;

	/**
	 * expanded = true vs contracted false. The spanning tree from this node down is
	 * not shown.
	 */
	public boolean expanded;

	/**
	 * baseref points to the graph line where the presented node is an agent node
	 * and it was first presented at the line indexed by this base reference it is
	 * expanded if at all there is thus a line with this backref set at other than
	 * -1 represents and link where the displayed node is it is not a leaf
	 */
	public NetLine baseref;

	/**
	 * expanded elsewhere if true it used to point then points to the graph line
	 * where the entity first appeared and is expaned there if it is not a leaf
	 */
	public boolean expandselsewhere;

	/*
	 * reference to the Node from which this displaynode of this line is spawned
	 */
	public Node adjacentnoderef;

	/**
	 * Reference to the line object of the adjacent lines. This was a big problem to
	 * get set correctly.
	 */
	public NetLine adjacentlineref;

	/**
	 * the level of this line - the 0 (zero) level is the focus. This spanning tree
	 * is BFS so level is the depth of the search
	 */
	public int level;

	/**
	 * leaf if false, branch if true probably obsolete and if not should be isLeaf
	 */
	public boolean branch;

	/**
	 * If true the line is visible. When plus or minus the tree is walked and lines
	 * are suppressed
	 */
	public boolean visible;

	/**
	 * plus = 1, minus = 2 neither = 3
	 */
	public int plusminusneither;

	/**
	 * references to Line(s)s above this line. Not used here but is public.
	 */
	public Vector<NetLine> hyperLineV = new Vector<NetLine>(4);

	/**
	 * references to Line(s) below this line. Not used here but is public.
	 */
	public Vector<NetLine> hypoLineV = new Vector<NetLine>(4);

	/*
	 * DOM Elements for Add2DOM
	 */
	private Element lineElem = null;
	private Element linedisplayelements = null;
	private Element linedisplayelement = null;

	// LineElement for LineDisplaySys
	private LineElement linedisplayelem = null;
	
	LineSpecSet linespecseta = null;
	TreeMap<Integer, LineElement> treemaphold = null;
	NetTreeControl nettreecontrola = null;
	Net settingsneta = null;


	/**
	 * <PRE>
	 * 	array of line drawing and vertical line information
	 * 	vertline[0][0-1] reserved for control information
	 * 		[0][0] num levels
	 * 		[0][1] 999 marker
	 * 		vertline[level][0] = linedrawing symbol
	 * 		vertline[level][1] = type of link 0=nolink-startnode, 1=HYPER, 2=HYPO
	 * 		vertline[level][2] = 0=not highlighted, 1=highlighted
	 * 
	 * 		It inherits most of this from its closest kin.
	 * 		The array is fixed till I get it all working then should be converted to
	 * 	 some Collection that can be walked as easily as arrays.
	 * 	 The [32][3] means a max of 32 levels and 3 entries as described above.
	 * 
	 * </PRE>
	 */
	public int[][] vertline = new int[200][3];

	private static int
	// FOCUS = -1,
	BLANK = 0, LINEUP = 1, LINEDOWN = 2, FIRSTUP = 3, LASTDOWN = 4, TEEUP = 5, TEEDOWN = 6, HYPER = 1, HYPO = 2,
			PLUS = 1, MINUS = 2, NEITHER = 3, LINEDRAW = 0, LINKTYPE = 1, HIGHLIGHT = 2,
			// YES = 1,
			NO = 0;

	int j, i;
	int intlineref;
	Integer Inthold;
	NetLine objlinehold;
	UserSession usersession;

	/**
	 * Tells if the line is the first pushed in the vector to determine if the + or
	 * minus sign is at the top or bottom of a set Another way to look at it is
	 * first, last, notfirstorlast. In other words it is either one of: first or
	 * last, or not first or last.
	 */
	boolean lastVertical;

	// ArrayList<GenProp> Props = new ArrayList<>();
	ArrayList<GenProp> displaynodeProps = new ArrayList<>();
	ArrayList<GenProp> adjacentnodeProps = new ArrayList<>();
	ArrayList<GenProp> linkrefProps = new ArrayList<>();
	Node nodeA = null;

	Node leftnode = null;
	Node rightnode = null;
	GenProp inlineprop = null;
	GenProp inlinelinkprop = null;
	Class<?> lineClass;
	Field[] lineFields;
	String dispnodestring = "";

	/**
	 * Constructor: The parameters are explained above.
	 */
	public NetLine(int linenoI, int levelI, Relationship linkrefI, Node adjacentnoderefI, String typeI,
			Node displaynodeI, boolean expandselsewhereI, NetLine adjacentlinerefI, boolean lastVerticalI,
			UserSession usersessionI) {

		this.lineno = linenoI;
		this.level = levelI;
		this.linkref = linkrefI;
		this.adjacentnoderef = adjacentnoderefI;
		this.type = typeI;
		this.expandselsewhere = expandselsewhereI;
		this.displaynode = displaynodeI;
		this.adjacentlineref = adjacentlinerefI;
		this.lastVertical = lastVerticalI;
		this.usersession = usersessionI;
		// usersession.log.debug("NL:before setting nodes:type:" + type);
		if (!type.equalsIgnoreCase("focus")) { // NOT NOT NOT
			leftnode = linkref.getStartNode();
			rightnode = linkref.getEndNode();
		}

		branch = false;
		visible = true;

		if (adjacentlineref != null) {
			adjacentlineref.branch = true;
		}

		vertline[0][0] = level;
		vertline[0][1] = 999;
		vertline[0][2] = 888;
		// expanded = false;
		if (adjacentlineref == null) {
			// the focus line do nothing
			// usersession.log.debug("adjacentlineref == null;");
		} else {
			// Copy the contents of the adjacent vertline array
			// to the current line with adjustments.
			// The concept is to inherit from ascendent/decendent (adjacent) Line.
			for (i = 1; i < level; i++) {
				if (type.equalsIgnoreCase("hyper")) { // current
					if (level == i + 1) { // level adjacent to current
						if (adjacentlineref.vertline[i][LINEDRAW] == FIRSTUP) {
							vertline[i][LINEDRAW] = BLANK;
						} else if (adjacentlineref.vertline[i][LINKTYPE] == HYPER) {
							vertline[i][LINEDRAW] = LINEUP;
						} else if (adjacentlineref.vertline[i][LINKTYPE] == HYPO) {
							vertline[i][LINEDRAW] = LINEDOWN;
						}
					} else { // straight copy - inherit
						vertline[i][LINEDRAW] = adjacentlineref.vertline[i][LINEDRAW];
					}
				} else if (type.equalsIgnoreCase("hypo")) { // current
					if (level == i + 1) { // level adjacent to current
						if (adjacentlineref.vertline[i][LINEDRAW] == LASTDOWN) {
							vertline[i][LINEDRAW] = BLANK;
						} else if (adjacentlineref.vertline[i][LINKTYPE] == HYPO) {
							vertline[i][LINEDRAW] = LINEDOWN;
						} else if (adjacentlineref.vertline[i][LINKTYPE] == HYPER) {
							vertline[i][LINEDRAW] = LINEUP;
						}
					} else { // straight copy - inherit
						vertline[i][LINEDRAW] = adjacentlineref.vertline[i][LINEDRAW];
					}
				} else {
					// usersession.log.debug("NL: ERROR: must be hypo or hyper");
				}
				vertline[i][LINKTYPE] = adjacentlineref.vertline[i][LINKTYPE];
				vertline[i][HIGHLIGHT] = adjacentlineref.vertline[i][HIGHLIGHT];
			} // for (copying the adjacent lines)

			// then add the current element level should be 1?
			// set vertline to vert values
			if (type.equalsIgnoreCase("hyper")) {
				if (lastVertical) {
					vertline[level][LINEDRAW] = FIRSTUP;
					vertline[level][LINKTYPE] = HYPER;
					vertline[level][HIGHLIGHT] = NO;
				} else {
					vertline[level][LINEDRAW] = TEEUP;
					vertline[level][LINKTYPE] = HYPER;
					vertline[level][HIGHLIGHT] = NO;
				}
			} else if (type.equalsIgnoreCase("hypo")) {
				if (lastVertical) {
					vertline[level][LINEDRAW] = LASTDOWN;
					vertline[level][LINKTYPE] = HYPO;
					vertline[level][HIGHLIGHT] = NO;
				} else {
					vertline[level][LINEDRAW] = TEEDOWN;
					vertline[level][LINKTYPE] = HYPO;
					vertline[level][HIGHLIGHT] = NO;
				}
			}
		} // if (adjacentlineref == null )
		this.lineClass = this.getClass();
		this.lineFields = lineClass.getDeclaredFields();
	} // constructor

	/**
	 * Add the Line to a DOM internal document.
	 * @throws ServiceException 
	 */
	public void add2DOM(Document doc, Element parent, String aliasI) throws ServiceException {
		// start a line
		usersession.log.debug("NL:a2d:top.");
		Element leftnodeElem = null;
		Element rightnodeElem = null;
		lineElem = doc.createElement("line");
		if (parent == null) {
			doc.appendChild(lineElem);
		} else {
			parent.appendChild(lineElem);
		}
		lineElem.setAttribute("type", this.type);
		if (this.expandselsewhere) {
			lineElem.setAttribute("expandelsewhere", "true");
		} else {
			lineElem.setAttribute("expandelsewhere", "false");
		}
		lineElem.setAttribute("LineNo", Integer.toString(this.getLineNo()));
		lineElem.setAttribute("level", Integer.toString(this.level));
		lineElem.setAttribute("plusminus", Integer.toString(this.plusminusneither));
		lineElem.setAttribute("visible", Boolean.toString(this.visible));
		usersession.log.debug("NL:a2d:before set nodeA to displaynode:id:" + displaynode.getId());
		nodeA = this.displaynode;
		String s1 = nodeA.getLabels().toString();
		String s2 = s1.replace('[', ' ');
		dispnodestring = s2.replace(']', ' ');
		dispnodestring = dispnodestring.trim();
		try {
			usersession.log.debug("NL:a2d:before set master node:string:" + dispnodestring + " displaynode:"
					+ displaynode.toString());
			if (dispnodestring.equalsIgnoreCase("Node")) {
				if (displaynode.hasProperty("Name")) {
					dispnodestring = displaynode.getProperty("Name").toString();
				} else {
					dispnodestring = "NameNotFound.";
				}
			}
		} catch (org.neo4j.graphdb.NotFoundException nfe) {
			usersession.log.error("NL:no property with key=Name or name.");
			dispnodestring = (Long.toString(displaynode.getId()));
		} catch (Exception e) {
			usersession.log.debug("NL:exception:" + e.toString());
		}
		if (dispnodestring.trim().equalsIgnoreCase("")) {
			if (displaynode.hasProperty("Name")) {
				dispnodestring = displaynode.getProperty("Name").toString();
			} else if (displaynode.hasProperty("name")) {
				dispnodestring = displaynode.getProperty("name").toString();
				messenger("NL:a2d:problem with name.");
			}
		}
		if (!this.type.equalsIgnoreCase("focus")) { // NOT NOT NOT NOT
			// usersession.log.debug("NL:type not equal to focus.");
			lineElem.setAttribute("adjacentnoderefid", (Long.toString(adjacentnoderef.getId())));
			try {
				lineElem.setAttribute("linkname", (String) linkref.getProperty("Name"));
				lineElem.setAttribute("MasterNode", String.valueOf(displaynode.getProperty("Name")));
				// usersession.log.debug("NL:MN:new.");
			} catch (org.neo4j.graphdb.NotFoundException nfe) {
				usersession.log.error("NL:NFE.");
				messenger("NL:a2d:NotFoundException:" + nfe.getMessage());
			} catch (NullPointerException npe) {
				lineElem.setAttribute("linkname", "Null");
				usersession.log.error("NL:linkname = null.");
				messenger("NL:a2d:linkname = null.");
			}
			try {
				lineElem.setAttribute("leftnodename", (String) linkref.getStartNode().getProperty("Name"));
			} catch (NullPointerException npe) {
				lineElem.setAttribute("leftnodename", "Null");
				usersession.log.error("NL:leftnodename = null.");
				messenger("NLa2d:leftnodename = null.");
			}
			try {
				lineElem.setAttribute("rightnodename", (String) linkref.getEndNode().getProperty("Name"));
			} catch (NullPointerException npe) {
				lineElem.setAttribute("rightnodename", "Null");
				usersession.log.error("NL:rightnodename = null.");
				messenger("NLa2d:rightnodename = null.");
			}
		} else { // it is the focus line
			try {
				usersession.log.debug("NL:focus line.");
				// 20200413
				// lineElem.setAttribute("MasterNode",
				// String.valueOf(displaynode.getProperty("Name")) );
				lineElem.setAttribute("MasterNode", dispnodestring);
			} catch (NullPointerException npe) {
				lineElem.setAttribute("Master", "NotFound");
				messenger("NLa2d:Master Not Found.");
				usersession.log.debug("NL:npe:" + npe.toString());
			}

		}
		if (nodeA.getId() != 0) {
			lineElem.setAttribute("nodeinternalid", (Long.toString(nodeA.getId())));
		} else {
			lineElem.setAttribute("nodeinternalid", "0");
		}
		// usersession.log.debug("NL:a2d:before linkinternalid.");
		if (linkref != null) {
			// usersession.log.debug("NL:a2d:before linkinternalid:" +
			// this.linkref.getId());
			lineElem.setAttribute("linkinternalid", (Long.toString(this.linkref.getId())));
		} else {
			// usersession.log.debug("NL:a2d:linkref==null.");
		}
		// usersession.log.debug("NL:a2d:before verticals.");
		// vertical line information first
		// vertline[0][0] contains the level (like a count, and what goes in the
		// position),
		Element vertElem = null;
		for (int i = 1; i <= this.vertline[0][0]; i++) {
			// was for ( int i = this.vertline[0][0]; i > 0 ; i-- ) {
			vertElem = doc.createElement("vert");
			lineElem.appendChild(vertElem);
			if (this.vertline[0][0] == 0 && i == 1) { // focus
				vertElem.setAttribute("type", "FOCUS");
			} else if (this.vertline[i][0] == BLANK) {
				vertElem.setAttribute("type", "BLANK");
			} else if (this.vertline[i][0] == LINEUP) {
				vertElem.setAttribute("type", "LINEUP");
			} else if ((this.vertline[i][0] == TEEUP) && (this.branch == true)) {
				vertElem.setAttribute("type", "TEEUP");
				vertElem.setAttribute("branch", "true");
				vertElem.setAttribute("LineNo", new Integer(this.getLineNo()).toString());
				if (this.plusminusneither == PLUS) {
					vertElem.setAttribute("plusminus", "PLUS");
				} else {
					vertElem.setAttribute("plusminus", "MINUS");
				}
			} else if ((this.vertline[i][0] == TEEUP) && (this.branch == false)) {
				vertElem.setAttribute("type", "TEEUP");
				vertElem.setAttribute("branch", "false");
			} else if (this.vertline[i][0] == LINEDOWN) {
				vertElem.setAttribute("type", "LINEDOWN");
				// );
			} else if ((this.vertline[i][0] == TEEDOWN) && (this.branch == true)) {
				vertElem.setAttribute("type", "TEEDOWN");
				vertElem.setAttribute("branch", "true");
				vertElem.setAttribute("LineNo", new Integer(this.getLineNo()).toString());
				if (this.plusminusneither == PLUS) {
					vertElem.setAttribute("plusminus", "PLUS");
				} else {
					vertElem.setAttribute("plusminus", "MINUS");
				}
			} else if ((this.vertline[i][0] == TEEDOWN) && (this.branch == false)) {
				vertElem.setAttribute("type", "TEEDOWN");
				vertElem.setAttribute("branch", "false");
			} else if ((this.vertline[i][0] == FIRSTUP) && (this.branch == true)) {
				vertElem.setAttribute("type", "FIRSTUP");
				vertElem.setAttribute("branch", "true");
				if (this.plusminusneither == PLUS) {
					vertElem.setAttribute("plusminus", "PLUS");
				} else {
					vertElem.setAttribute("plusminus", "MINUS");
				}
			} else if ((this.vertline[i][0] == FIRSTUP) && (this.branch == false)) {
				vertElem.setAttribute("type", "FIRSTUP");
				vertElem.setAttribute("branch", "false");
			} else if ((this.vertline[i][0] == LASTDOWN) && (this.branch == true)) {
				vertElem.setAttribute("type", "LASTDOWN");
				vertElem.setAttribute("branch", "true");
				if (this.plusminusneither == PLUS) {
					vertElem.setAttribute("plusminus", "PLUS");
				} else {
					vertElem.setAttribute("plusminus", "MINUS");
				}
			} else if ((this.vertline[i][0] == LASTDOWN) && (this.branch == false)) {
				vertElem.setAttribute("type", "LASTDOWN");
				vertElem.setAttribute("branch", "false");
			} else {
				usersession.log.error("NL:a2d:ERROR: did not handle vertline type.");
			}
		} // for vertical elements

		messenger("NL:Properties:after verticals.");
		// node props the displaynodeProps array was loaded at line creation: cat, name,
		// type, value
		String linespecnodeproptrigger = "";
		String cnetinterface = "";
		Boolean defaulttrigger = true;
		try {
			// messenger("NL:a2d:PropTrigger:" +
			// usersession.nettreecontrol.NTNet.getNetprop("LineSpecNodePropTrigger").value
			// );
			if (!usersession.nettreecontrol.NTNet.getNetprop("LineSpecNodeNameTrigger").value
					.equalsIgnoreCase("default")) {
				// NOT equal default
				defaulttrigger = false;
			}
		} catch (NetTreeException nte) {
			messenger("NL:nte:topprops:nte:" + nte.toString());
		}
		if (!type.equalsIgnoreCase("focus")) { // Not Not
			leftnodeElem = doc.createElement("leftnode");
			lineElem.appendChild(leftnodeElem);
			GenProp leftnodeprophold = null;
			String leftnodepropkey = "";
			Set<String> leftnodekeyset = (Set<String>) leftnode.getAllProperties().keySet();
			Iterator<String> leftnodekeyiter = leftnodekeyset.iterator();
			while (leftnodekeyiter.hasNext()) {
				leftnodepropkey = (String) leftnodekeyiter.next();
				leftnodeprophold = new GenProp("Node", leftnodepropkey, "String",
						leftnode.getProperty(leftnodepropkey).toString());
				leftnodeprophold.Add2DOM(doc, leftnodeElem, "nodeproperty");
			}

			rightnodeElem = doc.createElement("rightnode");
			lineElem.appendChild(rightnodeElem);

			GenProp rightnodeprophold = null;
			String rightnodepropkey = "";
			Set<String> rightnodekeyset = (Set<String>) rightnode.getAllProperties().keySet();
			Iterator<String> rightnodekeyiter = rightnodekeyset.iterator();
			while (rightnodekeyiter.hasNext()) {
				rightnodepropkey = rightnodekeyiter.next();
				rightnodeprophold = new GenProp("Node", rightnodepropkey, "String",
						rightnode.getProperty(rightnodepropkey).toString());
				rightnodeprophold.Add2DOM(doc, rightnodeElem, "nodeproperty");
			}
			GenProp linkrefprophold = null;
			Iterator<GenProp> linkrefpropiter = this.linkrefProps.iterator();
			while (linkrefpropiter.hasNext()) {
				linkrefprophold = linkrefpropiter.next();
				linkrefprophold.Add2DOM(doc, lineElem, "linkproperty");
			}
		} else {
			// focus
		}

		messenger("NL:lineSpec process.");
		if ( usersession.settingsnetset == false ) {
			messenger("NL:usersession.settingnetset:" + usersession.settingsnetset);
			linedisplayelements = doc.createElement("linedisplayelements");
			lineElem.appendChild(linedisplayelements);
			linedisplayelement = null;
			linedisplayelem = null;
			String LineSpecNodeNameTrigger;
			try {
				LineSpecNodeNameTrigger = usersession.nettreecontrol.NTNet.getNetprop("LineSpecNodeNameTrigger").value;
				this.nettreecontrola = usersession.nettreecontrol;
				if (usersession.nettreecontrol.NTNet.getNetprop("filename").value.equals("default.cnet")
						|| LineSpecNodeNameTrigger.equalsIgnoreCase("default")) {
					// messenger("NL:linespecsethash:keys:" +
					// nettreecontrola.linespecsethash.keySet().toString());
					linespecseta = usersession.nettreecontrol.linespecsethash.get("default");
					// messenger("NL:default:linespecseta:name:" + linespecseta.name);
					if (type.equalsIgnoreCase("hypo")) {
						createLineEntries(linespecseta.hypotreeMap, doc);
					} else if (type.equalsIgnoreCase("hyper")) {
						createLineEntries(linespecseta.hypertreeMap, doc);
					} else if (type.equalsIgnoreCase("focus")) {
						createLineEntries(linespecseta.focustreeMap, doc);
					} else {
						usersession.message4dom.messagewords.add("NL: skip focus:" + type);
					}
				} else { // not default

					// messenger("NL:linespecsethash.size():" +
					// nettreecontrola.linespecsethash.size());
					// messenger("NL:linespecsethash:keys:" +
					// nettreecontrola.linespecsethash.keySet().toString());
					// messenger("NL:LineSpecNodeNameTrigger:" +
					// nettreecontrola.linespecsethash.keySet().toString());
					// if the Nprop name equals LineSpecNodeNameTrigger use its value to select a
					// linespec from linespechash
					String lookingfor = this.displaynode.getProperty(LineSpecNodeNameTrigger).toString();
					usersession.log.error("NL:nte:lookingfor:" + lookingfor);
					Set<String> linespecsetiter = (Set<String>) nettreecontrola.linespecsethash.keySet();
					Iterator<String> linespeciter = linespecsetiter.iterator();
					String linespecitername = "";
					LineSpecSet linespecseta = null;
					while (linespeciter.hasNext()) {
						linespecitername = linespeciter.next();
						if (linespecitername.equals(lookingfor)) {
							linespecseta = usersession.nettreecontrol.linespecsethash.get(lookingfor);
							// messenger("NL:linespecitername:" + linespecitername);
							if (this.type.equalsIgnoreCase("hypo")) {
								createLineEntries(linespecseta.hypotreeMap, doc);
							} else if (type.equalsIgnoreCase("hyper")) {
								createLineEntries(linespecseta.hypertreeMap, doc);
							} else if (type.equalsIgnoreCase("focus")) {
								createLineEntries(linespecseta.focustreeMap, doc);
							} else {
								usersession.message4dom.messagewords.add("NL: skip focus:" + type);
							}
						}
					}
				}
			} catch (org.neo4j.graphdb.NotFoundException nfe) {
				usersession.message4dom.messagewords.add("Network missing control file required trigger property. ");
			} catch (NetTreeException nte) {
				messenger("NL:nte:" + nte.getMessage());
				usersession.log.error("NL:nte:" + nte.getMessage());
				nte.printStackTrace();
			}
		} else { // settingnetset == true
			messenger("NL:usersession.settingnetset:s/b true:" + usersession.settingsnetset);
			linedisplayelements = doc.createElement("linedisplayelements");
			lineElem.appendChild(linedisplayelements);
			linedisplayelement = null;
			linedisplayelem = null;
			String LineSpecNodeNameTrigger;
			try {
				LineSpecNodeNameTrigger = usersession.settingsnet.getNetprop("LineSpecNodeNameTrigger").value;
				messenger("NL:LineSpecNodeNameTrigger:" + usersession.settingsnet.getNetprop("LineSpecNodeNameTrigger").value);
				this.settingsneta = usersession.settingsnet;
				if (settingsneta.getNetprop("filename").value.equals("cntldefault.unet")
						|| LineSpecNodeNameTrigger.equalsIgnoreCase("default")) {
					messenger("NL:linespecsethash:keys:" + nettreecontrola.linespecsethash.keySet().toString());
					linespecseta = usersession.nettreecontrol.linespecsethash.get("default");
					messenger("NL:default:linespecseta:name:" + linespecseta.name);
					if (type.equalsIgnoreCase("hypo")) {
						createLineEntries(linespecseta.hypotreeMap, doc);
					} else if (type.equalsIgnoreCase("hyper")) {
						createLineEntries(linespecseta.hypertreeMap, doc);
					} else if (type.equalsIgnoreCase("focus")) {
						createLineEntries(linespecseta.focustreeMap, doc);
					} else {
						usersession.message4dom.messagewords.add("NL: skip focus:" + type);
					}
				} else { // not default
					// messenger("NL:linespecsethash.size():" +
					// nettreecontrola.linespecsethash.size());
					// messenger("NL:linespecsethash:keys:" +
					// nettreecontrola.linespecsethash.keySet().toString());
					// messenger("NL:LineSpecNodeNameTrigger:" +
					// nettreecontrola.linespecsethash.keySet().toString());
					// if the Nprop name equals LineSpecNodeNameTrigger use its value to select a
					// linespec from linespechash
					String lookingfor = this.displaynode.getProperty(LineSpecNodeNameTrigger).toString();
					usersession.log.error("NL:nte:lookingfor:" + lookingfor);
					Set<String> linespecsetiter = (Set<String>) nettreecontrola.linespecsethash.keySet();
					Iterator<String> linespeciter = linespecsetiter.iterator();
					String linespecitername = "";
					LineSpecSet linespecseta = null;
					while (linespeciter.hasNext()) {
						linespecitername = linespeciter.next();
						if (linespecitername.equals(lookingfor)) {
							linespecseta = usersession.nettreecontrol.linespecsethash.get(lookingfor);
							// messenger("NL:linespecitername:" + linespecitername);
							if (this.type.equalsIgnoreCase("hypo")) {
								createLineEntries(linespecseta.hypotreeMap, doc);
							} else if (type.equalsIgnoreCase("hyper")) {
								createLineEntries(linespecseta.hypertreeMap, doc);
							} else if (type.equalsIgnoreCase("focus")) {
								createLineEntries(linespecseta.focustreeMap, doc);
							} else {
								usersession.message4dom.messagewords.add("NL: skip focus:" + type);
							}
						}
					}
				}
			} catch (org.neo4j.graphdb.NotFoundException nfe) {
				usersession.message4dom.messagewords.add("Network missing control file required trigger property. ");
			} catch (NetTreeException nte) {
				messenger("NL:nte:" + nte.getMessage());
				usersession.log.error("NL:nte:" + nte.getMessage());
				nte.printStackTrace();
			}
		}
	} // Add2DOM

	// Using <linetype>TreeMaps from LineDisplaySys it builds the dom of line  elements
	private void createLineEntries(TreeMap<Integer, LineElement> treemapi, Document doc) {
		messenger("NL:createLineEntries: top.");
		for (Map.Entry<Integer, LineElement> entry : treemapi.entrySet()) {
			try {
				int key = entry.getKey();				
				linedisplayelem = entry.getValue();
				//messenger("NL:createlineentries:key/vaue:" + key + "/" + linedisplayelem);
				linedisplayelement = doc.createElement("linedisplayelement");
				if (linedisplayelem.type.equalsIgnoreCase("linetype")) {
					// do nothing
				} else if (linedisplayelem.type.equalsIgnoreCase("leftnodeprop")) {
					linedisplayelement.setAttribute("value", (String) this.leftnode.getProperty(linedisplayelem.value));
				} else if (linedisplayelem.type.equalsIgnoreCase("rightnodeprop")) {
					linedisplayelement.setAttribute("value",
							(String) this.rightnode.getProperty(linedisplayelem.value));
				} else if (linedisplayelem.type.equalsIgnoreCase("masternode")) {
					// 20200413 delt masternode / leftnode ???
					linedisplayelement.setAttribute("value", dispnodestring);
				} else if (linedisplayelem.type.equalsIgnoreCase("linkprop")) {
					linedisplayelement.setAttribute("value", (String) this.linkref.getProperty(linedisplayelem.value));
				} else {
					linedisplayelement.setAttribute("value", linedisplayelem.value);
				}
				linedisplayelements.appendChild(linedisplayelement);
				// usersession.log.debug("NL:a2d:\t<DisplayElement key=" + key + " type=\"" +
			} catch (org.neo4j.graphdb.NotFoundException nfe) {
				linedisplayelement.setAttribute("value", "..");
				// messenger("NL;a2D",
			} catch (NullPointerException npe) {
				usersession.log.error("NL:NPE:" + "NL:" + npe.toString());
			}
			messenger("NL:createLineEntries: bottom.");
		}
	} // createLineEntries

	public int getLineNo() {
		return lineno;
	}

	public String getLineNodeName() {
		return (Long.toString(displaynode.getId()));
	}

	private void messenger(String message) {
		// usersession.message4dom.messagewords.add(message);
		usersession.log.debug(message);
	}

} // NetLine
