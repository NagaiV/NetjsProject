'use strict'
/*
   classes: NetTree, NetLine, ToDo
   functions: listLinks, listLines, listLinksNotPresented(), isNodePresented    setNodePresented, listNodes, listNodesNotPresented,
   getNetTree, insertAbove, insertBelow, sleep, isLinkPresented, getLinesIndex, getIndexOfLinenumberFromLines
    O change isLinkPresented to map for performance <string, string> ???
    Don't think isLinkPresented is ever called. It is not but test for boolean in a forEach loop of lNP

ToDos:
    switching to lines index 20220520
    linksNotPresented is not efficient - needs drop ones presented and maybe index the left and right 20220503
    nodesNotPresented ditto
    Need to recurse if nodesNotPresent is not empty
    test for node not presented when creating a todo ??? maybe done
    dropDouble to not add linksNotPresented that have double quotes
    version 20221206 uncommented default function ver
*/
//exports.__esModule = true;
// exports.ToDo = exports.NetTree = exports.NetLine = void 0
//import Net_js_1 from "/home/bgarvey/javascript/Net.js"
import { Node, Link } from './Net.js'
var linksNotPresented = new Map()
// note: nodesNotPresented is an array
var lines = new Array()
var workline = null
var adjacentlineindex = 0
var startnode = null
var leftew = false
var rightew = false
var version = '20220928 adding back template in showTree.'
var Net

/*
This is called with the adjacent line linenumber
*/
function getIndexOfLinenumberFromLines(linenumberI) {
  var workingline
  var returnix = -1
  //console.log('gIOLFL:looking for index of this linenumber:' + linenumberI);
  for (var i = 0; i < lines.length; i++) {
    workingline = lines[i]
    returnix = lines.indexOf(workingline)
    /*
        console.log('gIOLFL:for:' + linenumberI + ' checking:ln:' 
           + workingline.lineno + ' returnix:' + returnix
            + ' adjlinenum:' + workingline.adjacentlinklineno);
        */
    if (workingline.lineno === linenumberI) {
      //console.log('gIOLFL:got it returning:returnix:' + returnix);
      return returnix
    }
  }
  //console.log('gIOLFL:did not get it returning unchanged val of -1 ???');
  return returnix
}

function defaultabove(line) {
  var a = ''
  if (line.leftEW) {
    a += '^'
  }
  a += line.link.leftnode.name + ' &lt;- ('
  a += line.link.rightnode.name
  a += ')'
  return a
}

function defaultbelow(line) {
  var a = ''
  if (line.rightEW) {
    a += '^'
  }
  a += line.link.rightnode.name
  a += ' -&gt; ('
  a += line.link.leftnode.name
  a += ')'
  return a
}

function socialnetabove(line) {
  var a = ''
  if (line.leftEW) {
    a += '^'
  }
  a += line.link.leftnode.name + ' -&gt; '
  var label = ''
  if (line.link.props.has('label')) {
    console.log('link does have a label property.')
    label = line.link.props.get('label').value
    a += label
  } else {
    console.log('link does NOT have a label property.')
  }
  a += ' ('
  a += line.link.rightnode.name
  a += ')'
  return a
}
function socialnetbelow(line) {
  var a = ''
  if (line.rightEW) {
    a += '^'
  }
  a += line.link.rightnode.name
  a += ' -&gt; '
  var label = ''
  if (line.link.props.has('label')) {
    console.log('link DOES have a label property.')
    label = line.link.props.get('label').value
    a += label
  } else {
    console.log('link does not have a label property.')
  }
  a += ' ('
  a += line.link.leftnode.name
  a += ')'
  return a
}

function createArray(length) {
  var arr = new Array(length || 0),
    i = length
  if (arguments.length > 1) {
    var args = Array.prototype.slice.call(arguments, 1)
    while (i--) arr[length - 1 - i] = createArray.apply(this, args)
  }
  return arr
}

export var NetLine = /** @class */ (function () {
  function NetLine(
    linenoI,
    levelI,
    typeI,
    linkI,
    adjacentlinklinenoI,
    leftEWI,
    rightEWI,
    TCBI
  ) {
    console.log('typeItypeI', typeI)

    this.lineno = linenoI
    this.level = levelI
    this.type = typeI
    this.link = linkI
    this.adjacentlinklineno = adjacentlinklinenoI
    this.leftEW = leftEWI // left elsewhere
    this.rightEW = rightEWI // right elsewhere
    this.TCB = TCBI // top, continuation, bottom ?
    /*
        console.log('NL:newNetLine:no:' + this.lineno + ' level:' 
           + this.level + ' type:' + this.type
           + ' adjacentlinklineno:' + this.adjacentlinklineno 
           + ' linkname:' + this.link.name 
           + ' \nleftEW:' + this.leftEW + ' rightEW:' + this.rightEW + ' TCB:' + this.TCB);
        
           */
    let vertlines = createArray(100, 3)
    let expanded = true
    let branch = false
    let visible = true
    return this
  }
  return NetLine
})()
//exports.NetLine = NetLine;

export var NetTree = /** @class */ (function () {
  function NetTree(startnodename, Net) {
    this.todos = []
    this.nodesNotPresented = null
    this.Net = Net
    var nodesNotPresented = []
    console.log('linksNotPresasdented', linksNotPresented)
    Net.links.forEach(function (valuee, keye) {
      linksNotPresented.set(keye, true) // lNP is a Map
    })
    Net.nodes.forEach(function (valuee, keye) {
      nodesNotPresented.push(valuee.name)
    })
    this.nodesNotPresented = nodesNotPresented

    return this
  } // NetTree constructor
  console.log('NetTreeNetTreeNetTree', NetTree.prototype)
  NetTree.prototype.showTree = function (netnetwork, nodeToBeRemoved) {
    try {
      console.log('nodeToBeRemoved', nodeToBeRemoved)
      console.log('IN SHOWTREE')
      console.log('netnetwowqerk', netnetwork)
      console.log('st:str:n.n:keys:' + JSON.stringify(netnetwork.nodes.keys))
      // console.log('st:n.n.p.size:' + netnetwork.netprops.size)
      console.log('st:n.n.p.size:' + netnetwork.props.size)
      console.log('st:NT:version:' + version) // version from schema

      const iterator1 = netnetwork.props.keys()
      // const iterator1 = netnetwork.netprops.keys()
      for (const item of iterator1) {
        console.log(item + ' value:' + JSON.stringify(netnetwork.getProp(item)))
      }

      var templateprop = ''
      if (netnetwork.props.has('template')) {
        var propA = netnetwork.getProp('template')
        templateprop = propA.value.value
      } else {
        templateprop = ''
        console.log('st:prop template not available.')
        console.log('st:n.n:props.size:' + netnetwork.props.size)
        console.log('st:n.props:' + JSON.stringify(netnetwork.props))
        console.log('st:n.p:keys:' + JSON.stringify(netnetwork.props.keys))
      }
      var rstr = ''
      rstr += '<br>'
      rstr +=
        '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"' +
        '"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">'
      rstr += '<head xmlns="http://www.w3.org/1999/xhtml">'
      rstr += '<style type="text/css">'
      rstr += 'a:link{text-decoration: none;}'
      rstr += '.a {text-decoration:none;}'
      rstr += '</style>'
      rstr +=
        '<meta content="width=device-width; initial-scale=1.0;' +
        ' maximum-scale=1.0; user-scalable=no;" name="viewport" />'
      rstr += '<meta content="width" name="MobileOptimized" />'
      rstr += '<meta content="true" name="HandheldFriendly" />'
      rstr += '<script>'
      rstr += 'function callMyFunction(data) {'
      rstr += 'var xhr = new XMLHttpRequest();'
      rstr += 'xhr.open("POST", "/myFunction");'
      rstr += 'xhr.send(JSON.stringify(data));'
      rstr += 'xhr.onreadystatechange = function ()'
      rstr += '{'
      rstr += 'console.log("xht response text",xhr.responseText);'
      rstr += 'document.body.innerHTML = xhr.responseText;'
      rstr += '}'
      rstr += '}'
      rstr += '</script>'

      rstr += '</head>'
      rstr += '<title xmlns="http://www.w3.org/1999/xhtml">NetTree</title>'
      rstr += '<b xmlns="http://www.w3.org/1999/xhtml" />'
      rstr += '<div xmlns="http://www.w3.org/1999/xhtml" class="graph">'

      lines.forEach(function (value, key) {
        if (
          nodeToBeRemoved &&
          value.lineno === nodeToBeRemoved.lineno &&
          value.level === nodeToBeRemoved.level
        ) {
        } else {
          rstr += '<code>'
          if (value.type == 'above') {
            rstr += '<div class="row">'
            rstr +=
              '&nbsp;&nbsp;'.repeat(value.level) +
              '   ' +
              '<span style="color:red">&lt; </span>' +
              '<span style="color:blue">'
            rstr +=
              '<a HREF="?service=NavCon&amp">' +
              '<font size="+0">&equiv;&nbsp;</font></a>' +
              '</span>' +
              '<span style="color:red">'
            if (templateprop === 'socialnet') {
              rstr += socialnetabove(value)
            } else {
              rstr += defaultabove(value)
            }
            rstr += '</span>'
          } else if (value.type == 'start') {
            rstr += '> ' + startnode.name + '    '
          } else if (value.type == 'below') {
            rstr += '<div class="row">'
            rstr += '&nbsp;&nbsp;'.repeat(value.level) + '  '
            rstr +=
              // '<a HREF="?service=NavCon&amp">' +
              "<a style='cursor:pointer' onclick='callMyFunction(" +
              JSON.stringify(value) +
              ")'>" +
              "<font size='+0'>&equiv;&nbsp;</font></a>"

            // </a>'
            rstr += '<span style="">'
            if (templateprop === 'socialnet') {
              //var call = eval(templateprop + "below").call(value);
              rstr += socialnetbelow(value)
            } else {
              rstr += defaultbelow(value)
            }
            rstr += '</span>'
          }
          rstr += '</div>'
          rstr += '</code>'
        }
      })
      rstr += '</code>'
      // rstr += '<button onClick="handleBtnClick()" >Delete first node</button>'
      rstr += '</div>'
      return rstr
    } catch (error) {
      console.log('error', error)
    }
  }

  NetTree.prototype.isLinkPresented = function (linkname) {
    console.log(
      'iLP:isLinkPresented:top:this linkname:' + JSON.stringify(linkname)
    )
    this.listLinksNotPresented()
    //console.log('iLP lnp.has:' + this.lnp.has(linkname))
    if (this.Net.links.has(linkname)) {
      //console.log('\niLP:it is a link, now test presented.');
      if (linksNotPresented.get(linkname)) {
        //console.log('iLP:get true the link has not been presented.')
        return true
      } else {
        //console.log('iLP:get returned false so HAS BEEN PRESENTED.')
        return false
      }
    } else {
      throw 'iLP:linkname:' + linkname + ' is not a link'
    }
  } // isLinkPresented

  NetTree.prototype.setLinkPresented = function (linkname) {
    //console.log('sLP:top:linkname:' + linkname);
    var lnp = 0
    if (this.Net.links.has(linkname)) {
      linksNotPresented.set(linkname, false)
    } else {
      throw 'sLP:linkname:' + linkname + ' is not a link'
    }
  } // setLinkPresented

  NetTree.prototype.listLinks = function () {
    console.log('\nLinks List')
    this.Net.links.forEach(function (value, key) {
      console.log('link.name:' + JSON.parse(JSON.stringify(value)).name)
    })
  }

  NetTree.prototype.listLines = function () {
    //console.log('\nLines List (the key is the index) len:' + lines.length);
    lines.forEach(function (value, key) {
      //console.log('line:key|index:' + key + ' value|lineObj:'
      //+ JSON.stringify(value) + '\n');
    })
  }

  NetTree.prototype.space = function (num) {
    for (var n = 0; n < num; n++) {
      console.log('   ')
    }
  }

  NetTree.prototype.listToDos = function () {
    console.log('\nToDos List len:' + this.todos.length)
    for (var t = 0; t < this.todos.length; t++) {
      console.log(
        'todo.level:' +
          this.todos[t].level +
          ' nodename:' +
          this.todos[t].nodename +
          ' adjacentlinenumber:' +
          this.todos[t].adjacentlinenumber
      )
    }
    console.log('\n')
  }

  NetTree.prototype.listLinksNotPresented = function () {
    console.log('\n--listLinksNotPresented -- len:' + linksNotPresented.size)
    linksNotPresented.forEach(function (value, key, map) {
      console.log('value/key:' + value + '/' + key)
    })
    console.log('\n')
  } // listLinksNotPresented

  NetTree.prototype.isNodePresented = function (nodename) {
    //console.log('iNP:top:nodename:' + nodename);
    //console.log('iNP:Net.nodes.has:' + this.Net.nodes.has(nodename));
    if (this.Net.nodes.has(nodename)) {
      if (this.nodesNotPresented.includes(nodename)) {
        //console.log('iNP:returning false. nodename was in nnp array');
        return false
      } else {
        //console.log('iNP:returning true. nodename was not in nnp array');
        return true
      }
    } else {
      throw 'iNP:Not a node.'
    }
  } // isNodePresented

  NetTree.prototype.setNodePresented = function (nodename) {
    //console.log('nodename:' + nodename);
    var nnp = 0
    if (this.Net.nodes.has(nodename)) {
      if (this.nodesNotPresented.includes(nodename)) {
        nnp = this.nodesNotPresented.indexOf(nodename)
        this.nodesNotPresented.splice(nnp, 1)
        //console.log('sNP:returning true. Should have been deleted.');
        //this.listNodesNotPresented
        return true
      } else {
        //console.log('sNP:returning false. Was not in nnp array.');
        //this.listNodesNotPresented()
        return false
      }
    } else {
      throw 'sNP:Not a node.'
    }
  } // setNodePresented

  NetTree.prototype.listNodes = function () {
    console.log('\nNodes List   size:' + this.Net.nodes.size)
    this.Net.links.forEach(function (value, key) {
      console.log('node.name:' + JSON.parse(JSON.stringify(key)).name)
    })
  }

  NetTree.prototype.listNodesNotPresented = function () {
    console.log(
      '\nnodesNotPresented List: length:' + this.nodesNotPresented.length
    )
    this.nodesNotPresented.forEach(function (value, key) {
      console.log('nnp:key/val:' + key + '/' + value)
    })
  } // listNodesNotPresented

  NetTree.prototype.getNetTree = function (startnodename, Net) {
    var _this = this
    //console.log('\nIn getNetTree version:' + this.version);
    //console.log('NT:gNT:startnodename:' + JSON.stringify(startnodename));
    //startnodename = 'Albert'
    lines = new Array()
    //console.log('getNetTree:top:this.Net.links.size:' + this.Net.links.size);
    this.Net = Net
    if (startnodename === '' || startnodename === null) {
      //console.log('gNT:startnodename is blank or null.');
      var nodeiter = Net.nodes.entries()
      console.log('nodeiter', nodeiter)
      startnode = nodeiter.next().value
    } else {
      startnode = Net.nodes.get(startnodename)
    }
    //console.log('gNT:startnodesection: startnodename:' + startnodename
    //+ ' startnode.name:' + startnode.name);
    var lineno = 0
    var level = 0
    var templink = new Link('start-start', new Node('left'), new Node('right'))
    //var templink = new Net.Link('start-start', new Net.Node('left'), new Net.Node('right'));
    // l l t l, adj, l, r, T
    lineno++
    workline = new NetLine(
      lineno,
      level,
      'start',
      templink,
      -1,
      false,
      false,
      ''
    )
    lines.push(workline)
    var found = lines.findIndex(function (nline, index) {
      //console.log('gNT:lfi:what is this for ????');
      if (nline.type == 'start') {
        console.log('gNT:lfi:found start. index:' + index + ' found:' + found)
      } else {
        console.log('gNT:lfi: NOT found start. ' + index + ' found:' + found)
      }
    })
    //this.listLines();
    //console.log('gNT:linecnt:' + lines.length);
    var target = new ToDo(level, startnode.name, 1)
    this.todos.push(target)
    var worklink = null
    this.setNodePresented(target.nodename)
    //console.log('getNetTree:before loops:Net.links.size:' + Net.links.size);
    while (this.nodesNotPresented.length > 0) {
      // nodes not presented
      //console.log('gNT:nnp.length:' + this.nodesNotPresented.length);
      // WHILE MORE TODOS
      while (this.todos.length > 0) {
        //console.log('todos.length:' + this.todos.length
        //+ ' is GREATER than 0.');
        //this.listToDos();
        // GET NEXT TODO
        target = this.todos.shift() //  returns and removes the first element (enqueue FIFO)
        console.log('target', target)
        console.log('this.todos', this.todos)
        /*
                console.log('just dequeued this todo target:' 
                + JSON.stringify(target));
                console.log('target.level:' + target.level + ' level:' + level
                    + ' target.adjacentlinenumber:' + target.adjacentlinenumber  
                    + ' target.nodename:' + target.nodename);
                */
        if (target.level != level) {
          //console.log('Changing level ' + level + ' to target.level ' + target.level);
          level = target.level
        }
        // FOR EACH LINK NOT PRESENTED
        linksNotPresented.forEach(function (value, key, map) {
          //console.log('####### top of lNP.forEach: key:'
          //+ key + ' value:' + value)
          leftew = false
          rightew = false
          if (value) {
            console.log('_this', _this)
            //
            worklink = _this.Net.links.get(key)
            console.log('workli123nk', worklink)
            if (worklink.leftnode.name == target.nodename) {
              /*
                            console.log('Below:worklink.name:' + worklink.name 
                            + ' leftnode.name:' + worklink.leftnode.name
                            + ' target.nodename:' + target.nodename
                            + ' rightnode.name:' + worklink.rightnode.name 
                            + ' target.adjacentlinenumber:' 
                            + target.adjacentlinenumber);
                            console.log('leftnode == targetnode EQUAL');
                            */
              lineno++
              adjacentlineindex = getIndexOfLinenumberFromLines(
                target.adjacentlinenumber
              )
              /*
                            console.log('Below:before new NetLine:' 
                            + 'adjacentlineindex:' + adjacentlineindex 
                            + ' plus 1');
                            */
              if (_this.isNodePresented(worklink.leftnode.name)) {
                leftew = true
              }
              workline = new NetLine(
                lineno,
                level + 1,
                'below',
                worklink,
                target.adjacentlinenumber,
                leftew,
                rightew,
                ''
              )
              lines.splice(adjacentlineindex + 1, 0, workline) // -1 no
              if (!_this.isNodePresented(worklink.rightnode.name)) {
                // NOT presented
                _this.todos.push(
                  new ToDo(level + 1, worklink.rightnode.name, lineno)
                )
                /*
                                console.log('**** Below:todos.pushed level:' 
                                + (level + 1) + ' rightnode:' 
                                + worklink.rightnode.name
                                + ' adjlineno:' + target.adjacentlinenumber);
                                */
              }
              //_this.listToDos();
              _this.setLinkPresented(worklink.name)
            }
            if (worklink.rightnode.name === target.nodename) {
              console.log('worklink.rightnode.name', worklink.rightnode.name)
              console.log('target.nodename', target.nodename)
              /*
                            console.log('Above:worklink.name:' + worklink.name 
                            + ' rightnode.name:' + worklink.rightnode.name 
                            + ' target.name:' + target.nodename
                            + ' leftnode.name:' + worklink.leftnode.name);
                            console.log('rightnode == targetnode EQUAL');
                            */
              lineno++
              adjacentlineindex = getIndexOfLinenumberFromLines(
                target.adjacentlinenumber
              )
              /*
                            console.log('Above:before new NetLine:'
                            + 'adjacentlineindex:' + adjacentlineindex 
                            + ' as (adjacentlineindex-1).');
                            */
              if (_this.isNodePresented(worklink.rightnode.name)) {
                rightew = true
              }
              workline = new NetLine(
                lineno,
                level + 1,
                'above',
                worklink,
                adjacentlineindex,
                leftew,
                rightew,
                ''
              )
              lines.splice(adjacentlineindex, 0, workline) // 20220525 no +-
              if (!_this.isNodePresented(worklink.leftnode.name)) {
                // node NOT presented
                _this.todos.push(
                  new ToDo(level + 1, worklink.leftnode.name, lineno)
                )
                /*
                                console.log('**** Above:todos.pushed level:' 
                                + (level + 1) + ' leftnode:' 
                                + worklink.leftnode.name
                                + ' target.adjacentlinenumber:' 
                                + target.adjacentlinenumber 
                                + ' worklink.name:' + worklink.name);
                                */
              }
              //_this.listToDos();
              _this.setLinkPresented(worklink.name)
            }
          } else {
            //console.log('key is:' + key
            //+ ' value NOT true therefore link was presented.');
          }
        }) // testing links not presented againt target.node
        //this.listLines();
        //this.listToDos();
        //console.log('level:' + level);
        //console.log('before sNP:target.nodename:' + target.nodename);
        this.setNodePresented(target.nodename)
      } // while ( this.todos.length > 0 ) {
      //this.listLines()
      // if no ToDos and still nodes start over  ?????  needs work
    } // nodesNotPresented.length > 0
    return lines
  } // getNetTree

  // NetTree.prototype.deleteNode = function () {

  // }

  return NetTree
})() // NetTree class
// exports.NetTree = NetTree

export var ToDo = /** @class */ (function () {
  function ToDo(levelI, nodenameI, adjacentlinenumberI) {
    this.level = levelI
    this.nodename = nodenameI
    this.adjacentlinenumber = adjacentlinenumberI
    //console.log('ToDo:new:level:' + this.level + ' nodename:'
    //+ this.nodename + ' adjacentlinenumber:' + this.adjacentlinenumber);
  }
  return ToDo
})() // NetTree
// exports.ToDo = ToDo

export default function ver() {
  return version
}

//module.exports = { NetLine: NetLine, NetTree: NetTree, ToDo: ToDo, ver: ver };
