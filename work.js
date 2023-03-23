/*
 work.js
 purpose to read in a .unet json file, 
 create a n.n.network, 
 present the contents as a nettree

 runs at http://localhost:3030?url=http://localhost/socialnet.unet
 var version = '20220428 attempt to create a NetTree and list lines.'
 var version = '202200525 930 RBG got basic showTree working. Now no startnode.'
 
 read in a .unet json file from a url
 JSON.parse the file into json object named parsedunet
 create a new net.nettree.Network as netnetwork
 walk through parsedunet adding props, nodes and links to netnetwork
 concat into string x the tables representing the network to form the response
 get the lines from the nettree and display them including the line drawing
 
 
*/

'use strict'

import fs from 'fs'
import http from 'http'
import url from 'url'
import querystring from 'querystring'
import request from 'request'
import { Network, Prop, Node, Link } from './Net.js'
import { NetTree } from './NetTree.js'

// console.log('NetTree123123', NetTree.prototype)

// import dll from './dll.js'
import { Console } from 'console'
var version = '20221024 0900 add popup. Requires loop and interperter.'

function consoler(message) {
  console.log(message)
}

const myData = {
  network: {
    name: 'socialnet.unet',
    netprops: [
      {
        NetworkKnownAs: 'Network',
        NodeKnownAs: 'Node',
        LinkKnownAs: 'Link',
        version: '20200820 1200.',
        Place: 'Holder',
      },
    ],
    nodes: [
      {
        name: 'Albert',
        nprops: [
          {
            Name: 'Albert',
          },
        ],
      },
      {
        name: 'Ben',
        nprops: [
          {
            Name: 'Ben',
          },
        ],
      },
      {
        name: 'Candice',
        nprops: [
          {
            Name: 'Candice',
          },
        ],
      },
      {
        name: 'Dwight',
        nprops: [
          {
            Name: 'Dwight',
          },
        ],
      },
      {
        name: 'Edward',
        nprops: [
          {
            Name: 'Edward',
          },
        ],
      },
      {
        name: 'Florence',
        nprops: [
          {
            Name: 'Florence',
          },
        ],
      },
      {
        name: 'George',
        nprops: [
          {
            Name: 'George',
          },
        ],
      },
      {
        name: 'Helen',
        nprops: [
          {
            Name: 'Helen',
          },
        ],
      },
      {
        name: 'Izzy',
        nprops: [
          {
            Name: 'Izzy',
          },
        ],
      },
    ],
    links: [
      {
        name: 'Albert-Ben',
        leftnode: 'Albert',
        rightnode: 'Ben',
        lprops: [
          {
            Name: 'Albert-Ben',
            label: 'Cooks for',
          },
        ],
      },
      {
        name: 'Albert-Candice',
        leftnode: 'Albert',
        rightnode: 'Candice',
        lprops: [
          {
            label: 'Plays Cards with',
            Name: 'Albert-Candice',
          },
        ],
      },
      {
        name: 'Ben-Dwight',
        leftnode: 'Ben',
        rightnode: 'Dwight',
        lprops: [
          {
            Name: 'Ben-Dwight',
            label: 'Swims with',
          },
        ],
      },
      {
        name: 'Ben-Edward',
        leftnode: 'Ben',
        rightnode: 'Edward',
        lprops: [
          {
            Name: 'Ben-Edward',
            label: 'Coaches',
          },
        ],
      },
      {
        name: 'Candice-Florence',
        leftnode: 'Candice',
        rightnode: 'Florence',
        lprops: [
          {
            label: 'Likes',
            Name: 'Candice-Florence',
          },
        ],
      },
      {
        name: 'Candice-George',
        leftnode: 'Candice',
        rightnode: 'George',
        lprops: [
          {
            Name: 'Candice-George',
            label: 'Sits by',
          },
        ],
      },
      {
        name: 'Edward-Candice',
        leftnode: 'Edward',
        rightnode: 'Candice',
        lprops: [
          {
            Name: 'Edward-Candice',
            label: 'Sits by',
          },
        ],
      },
      {
        name: 'Edward-Helen',
        leftnode: 'Edward',
        rightnode: 'Helen',
        lprops: [
          {
            Name: 'Edward-Helen',
            label: 'Likes',
          },
        ],
      },
      {
        name: 'Florence-Edward',
        leftnode: 'Florence',
        rightnode: 'Edward',
        lprops: [
          {
            label: 'Likes',
            Name: 'Florence-Edward',
          },
        ],
      },
      {
        name: 'Florence-Izzy',
        leftnode: 'Florence',
        rightnode: 'Izzy',
        lprops: [
          {
            Name: 'Florence-Izzy',
            label: "doesn't know",
          },
        ],
      },
      {
        name: 'George-Izzy',
        leftnode: 'George',
        rightnode: 'Izzy',
        lprops: [
          {
            Name: 'George-Izzy',
            label: 'Lives by',
          },
        ],
      },
      {
        name: 'Izzy-Helen',
        leftnode: 'Izzy',
        rightnode: 'Helen',
        lprops: [
          {
            Name: 'Izzy-Helen',
            label: 'Cooks for',
          },
        ],
      },
    ],
  },
}

var netpropA,
  n,
  np,
  l,
  lp,
  x = ''
var nodeA = null
var nodepropA = null
var linkA = null
var linkpropA = null
var key = ''
var value = ''
var type = ''
var compressedNode = []

var port = 3000
var server = http.createServer(function (req, res) {
  // console.log('req.url', req.url)

  const handleTreeStrucutre = (body, nodeToBeRemoved) => {
    var parsedunet = JSON.parse(String(body))
    x = ''
    x = ''
    x += '<html>'
    x += '<head>'
    x += '<style>'
    x += 'nav ul{height:200px; width:18%;}'
    x += 'nav ul{overflow:hidden; overflow-y:scroll;}'
    x += '</style>'
    x +=
      '<link rel="stylesheet" type="text/css" href="http://localhost/server.css">'
    x += '<div id="like_button_container"></div>'

    x +=
      '<script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>'
    x +=
      '<script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>'

    x += '</head>'
    var netnetwork = new Network(parsedunet.network.name)

    // loop through netprops and save in props in network
    for (var netp = 0; netp < parsedunet.network.netprops.length; netp++) {
      for (const [key, value] of Object.entries(
        parsedunet.network.netprops[netp]
      )) {
        netnetwork.addProp(key, new Prop(key, value))
      }
    }
    for (var n = 0; n < parsedunet.network.nodes.length; n++) {
      nodeA = new Node(parsedunet.network.nodes[n].name)
      netnetwork.nodes.set(nodeA.name, nodeA)
      console.log(
        'netnetwork.nodes.set(nodeA.name, nodeA)',
        netnetwork.nodes.set(nodeA.name, nodeA)
      )
      for (np = 0; np < parsedunet.network.nodes[n].nprops.length; np++) {
        for (const [key, value] of Object.entries(
          parsedunet.network.nodes[n].nprops[np]
        )) {
          nodeA.addProp(key, value)
        }
      }

      if (parsedunet.network.nodes.length - 1 === n) {
        console.log('nodeAName', nodeA.name)
        console.log('nodeAprops', nodeA.props)
      }
    }
    for (var l = 0; l < parsedunet.network.links.length; l++) {
      linkA = new Link(
        parsedunet.network.links[l].name,
        new Node(parsedunet.network.links[l].leftnode),
        new Node(parsedunet.network.links[l].rightnode)
      )
      netnetwork.links.set(linkA.name, linkA)
      for (var lp = 0; lp < parsedunet.network.links[l].lprops.length; lp++) {
        for (const [key, value] of Object.entries(
          parsedunet.network.links[l].lprops[lp]
        )) {
          linkA.addProp(key, value)
        }
      }
    }

    try {
      var startnode = netnetwork.getNode('Albert')

      let testtree = new NetTree('Albert', netnetwork)

      testtree.listLinksNotPresented()
      testtree.listNodesNotPresented()
      testtree.listLines()

      let somelines = testtree.getNetTree(startnode.name, netnetwork)
      x += testtree.showTree(testtree.Net, nodeToBeRemoved)
    } catch (err) {
      console.log('error', err)
    }
  }

  const myFunction = (nodeToBeRemoved) => {
    console.log('nodeTo434BeRemoved', nodeToBeRemoved)
    console.log('myDatamyData', myData.network.links)

    //Write logic to add all child and parent nodes to be compressed
    // let currentNode =

    return
    // code to remove the selected node
    // try {
    //   x = ''
    //   handleTreeStrucutre(JSON.stringify(myData), nodeToBeRemoved)
    //   x += '</body>'
    //   x += '</html>'
    //   res.writeHead(200, { 'Content-Type': 'text/html' })
    //   res.write(x)
    //   res.end('')
    // } catch (error) {
    //   console.log('error', error)
    // }
  }
  if (req.url === '/myFunction') {
    let body = ''
    req.on('data', (chunk) => {
      body += chunk
    })
    req.on('end', async () => {
      // This will output the received data to the console
      // res.end('Data received successfully');
      const nodeToBeRemoved = await JSON.parse(body)
      myFunction(nodeToBeRemoved)
    })
    req.on('error', (error) => {
      console.log('error', error)
    })
    return
  }

  const queryObject = url.parse(req.url, true).query
  // console.log('queryObject:' + JSON.stringify(queryObject))
  // console.log('work.js:version:' + version)
  var queryobj = querystring.parse(querystring.stringify(queryObject))
  // console.log('work.js:queryobj:' + JSON.stringify(queryobj))
  if (queryobj.url !== '') {
    request({ uri: queryobj.url }, function (error, response, body) {
      // console.log('body inside', body)
      if (typeof body !== 'undefined') {
        try {
          handleTreeStrucutre(body, {})
          //   var parsedunet = JSON.parse(String(body))
          //   x = ''
          //   x = ''
          //   x += '<html>'
          //   x += '<head>'
          //   x += '<style>'
          //   x += 'nav ul{height:200px; width:18%;}'
          //   x += 'nav ul{overflow:hidden; overflow-y:scroll;}'
          //   x += '</style>'
          //   x +=
          //     '<link rel="stylesheet" type="text/css" href="http://localhost/server.css">'
          //   x += '<div id="like_button_container"></div>'

          //   x +=
          //     '<script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>'
          //   x +=
          //     '<script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>'

          //   x += '</head>'
          //   console.log('network.name:' + JSON.stringify(parsedunet))
          //   // console.log('work.js:version:' + version)
          //   //  console.log('NetNet:' + Net)
          //   var netnetwork = new Network(parsedunet.network.name)

          //   // loop through netprops and save in props in network
          //   for (
          //     var netp = 0;
          //     netp < parsedunet.network.netprops.length;
          //     netp++
          //   ) {
          //     for (const [key, value] of Object.entries(
          //       parsedunet.network.netprops[netp]
          //     )) {
          //       // console.log('key:' + key + ' value:' + value)
          //       netnetwork.addProp(key, new Prop(key, value))
          //       //netnetwork.addProp(key, value);
          //     }
          //   }

          //   // loop through nodes and save in props in network
          //   for (var n = 0; n < parsedunet.network.nodes.length; n++) {
          //     nodeA = new Node(parsedunet.network.nodes[n].name)
          //     netnetwork.nodes.set(nodeA.name, nodeA)
          //     console.log(
          //       'netnetwork.nodes.set(nodeA.name, nodeA)',
          //       netnetwork.nodes.set(nodeA.name, nodeA)
          //     )
          //     for (np = 0; np < parsedunet.network.nodes[n].nprops.length; np++) {
          //       for (const [key, value] of Object.entries(
          //         parsedunet.network.nodes[n].nprops[np]
          //       )) {
          //         nodeA.addProp(key, value)
          //       }
          //     }

          //     if (parsedunet.network.nodes.length - 1 === n) {
          //       console.log('nodeAName', nodeA.name)
          //       console.log('nodeAprops', nodeA.props)
          //     }
          //   }
          //   for (var l = 0; l < parsedunet.network.links.length; l++) {
          //     linkA = new Link(
          //       parsedunet.network.links[l].name,
          //       new Node(parsedunet.network.links[l].leftnode),
          //       new Node(parsedunet.network.links[l].rightnode)
          //     )
          //     netnetwork.links.set(linkA.name, linkA)
          //     for (
          //       var lp = 0;
          //       lp < parsedunet.network.links[l].lprops.length;
          //       lp++
          //     ) {
          //       for (const [key, value] of Object.entries(
          //         parsedunet.network.links[l].lprops[lp]
          //       )) {
          //         linkA.addProp(key, value)
          //       }
          //     }
          //   }
          //   /*
          //     x += "<BR><br>Num Nodes:" + netnetwork.nodes.size
          //     x += "<BR>Num Links:" + netnetwork.links.size
          //     x += "<BR>Num NetProps:" + netnetwork.props.size
          //     x += '<BR>'
          //     x += '<BR>'
          //     x += '<table>'
          //     x += '<tr class=Net >'
          //        + '<td>Net:' + netnetwork.name + '</td>'
          //        + '</tr>'
          //        + '</table>'
          //     x += '<table>'
          //     netnetwork.props.forEach(function (netprop, npkey) {
          //        console.log('netprops:JString:netprop/npkey:' + JSON.stringify(netprop) + '/' + JSON.stringify(npkey))
          //        x += '<tr class=\"NetProps\" >'
          //            + '<td>' + '     ' + '</td>'
          //            + '<td>' + netprop.key + '</td>'
          //            + '<td>' + netprop.value + '</td>'
          //        x += '</tr>'
          //    })
          //     x += '</table>'
          //     netnetwork.nodes.forEach(function (node, nkey) {
          //        //console.log('nodes:nkey/node:' + JSON.stringify(nkey) + ' / ' + JSON.stringify(node))
          //        x += '<table>'
          //        x += '<tr class=\"Node\" >'
          //          + '<td>Node:' + node.name + '</td>'
          //          + '</tr>'
          //        x += '</table>'
          //        x += '<table>'
          //        let prophold = null
          //        const propiter = node.props.values()
          //        while (  prophold = propiter.next().value ) {
          //           console.log('prop:k/v:' + prophold.key + '/' + prophold.value)
          //          x += '<tr class=\"NodeProps\" >'
          //            + '<td>' + '     ' + '</td>'
          //            + '<td>' + prophold.key + '</td>'
          //            + '<td>' + prophold.value + '</td>'
          //            + '</tr>'
          //        }
          //        x += '</table>'
          //     })
          //     */
          //   /*
          //     netnetwork.links.forEach(function (litem, lkey) {
          //        //console.log('links:key/item:' + JSON.stringify(lkey) + ' / ' + JSON.stringify(litem))
          //          x += '<table>'
          //          x += '<tr class=\"Link\" >'
          //          + '<td>Link:L:' + litem.leftnode.name + '</td>'
          //          + '<td> R:' + litem.rightnode.name + '</td>'
          //          + '</tr>'
          //          x += '</table>'
          //        x += '<table>'

          //        let prophold = null
          //        const propiter = litem.props.values()
          //        while (  prophold = propiter.next().value ) {
          //           //console.log('prop:k/v:' + prophold.key + '/' + prophold.value)
          //           //console.log('links:props:key/item:' + JSON.stringify(lpkey) + ' / ' + JSON.stringify(prop))
          //          x += '<tr class=\"LinkProps\" >'
          //          + '<td>' + '     ' + '</td>'
          //          + '<td>' + prophold.key + '</td>'
          //          + '<td>' + prophold.value + '</td>'
          //          + '</tr>'
          //        }
          //        x += '</table>'
          //     })
          //     x += '</table>'
          //     */
          //   /*
          //     var dllb = new dll.DoublyLinkedList()
          //     netnetwork.links.forEach(function(link,dllkey) {
          //        x += 'Link:L:' + link.leftnode.name + ' R:' + link.rightnode.name
          //        dllb.add({'Up': link.leftnode.name, 'Down': link.rightnode.name})
          //        x += '<br/>'
          //     })
          //     x += '<br/>Doubly Linked List of Links'
          //     x += '<br/>'
          //     dllb.values
          //     for (const link of dllb.values()) {
          //        x += 'Link U:' + link.Up + ' D:' + link.Down
          //        x += '<br/>'
          //     }
          //     */

          try {
            //     /*
            //        console.log('netnetname:' + netnetwork.name)    //
            //        console.log('Num nodes:' + netnetwork.nodes.size)
            //        console.log('Num Links:'+ netnetwork.links.size)
            //        console.log('Num NetProps:' + netnetwork.props.size)
            //        console.log('Before new NetTree. Setting startnode to Albert.')
            //        */
            //     console.log('After all operations', netnetwork)
            //     var startnode = netnetwork.getNode('Albert')
            //     //var startnode = ''
            //     let testtree = new NetTree('Albert', netnetwork)
            //     console.log('t123esttree123', testtree)
            //     testtree.listLinksNotPresented()
            //     testtree.listNodesNotPresented()
            //     testtree.listLines()
            //     // console.log(
            //     //   'work:Before getNetTree:start/net:' +
            //     //     startnode.name +
            //     //     '/' +
            //     //     netnetwork.name
            //     // )
            //     //let somelines = testtree.getNetTree(startnode, netnetwork)
            //     let somelines = testtree.getNetTree(startnode.name, netnetwork)
            //     // console.log('work:After getNetTree:')
            //     // console.log('work:Before showTree')
            //     // console.log('work:testtree1111')
            //     // console.log('___________________________')
            //     console.log('befoooore xxxx showTree', x)
            //     x += testtree.showTree(testtree.Net)
            //     console.log('after xxxx showTree', x)
            //     // x += testtree.showTree({
            //     //   name: 'NetworkER',
            //     //   netprops: [
            //     //     {
            //     //       NetworkKnownAs: 'Network',
            //     //       NodeKnownAs: 'Node',
            //     //       LinkKnownAs: 'Link',
            //     //     },
            //     //   ],
            //     //   nodes: [
            //     //     { name: 'Link', nprops: [{ color: 'green' }] },
            //     //     { name: 'LinkProp', nprops: [{ color: 'yellow' }] },
            //     //     { name: 'Node', nprops: [{ color: 'lightblue' }] },
            //     //     { name: 'NetProp', nprops: [{ color: 'yellow' }] },
            //     //     { name: 'Network', nprops: [{ color: 'pink' }] },
            //     //     { name: 'NodeProp', nprops: [{ color: 'yellow' }] },
            //     //   ],
            //     //   links: [
            //     //     {
            //     //       name: 'L-LinkProp',
            //     //       leftnode: 'Link',
            //     //       rightnode: 'LinkProp',
            //     //       lprops: [{ Name: 'L-LinkProp' }],
            //     //     },
            //     //     {
            //     //       name: 'L-N',
            //     //       leftnode: 'Link',
            //     //       rightnode: 'Node',
            //     //       lprops: [{ Name: 'L-N', label: 'right' }],
            //     //     },
            //     //     {
            //     //       name: 'LinkProp-NetProp',
            //     //       leftnode: 'LinkProp',
            //     //       rightnode: 'NetProp',
            //     //       lprops: [{ Name: 'LinkProp-NetProp' }],
            //     //     },
            //     //     {
            //     //       name: 'N-L',
            //     //       leftnode: 'Node',
            //     //       rightnode: 'L',
            //     //       lprops: [{ Name: 'N-L' }],
            //     //     },
            //     //     {
            //     //       name: 'N-NodeProp',
            //     //       leftnode: 'Node',
            //     //       rightnode: 'NodeProp',
            //     //       lprops: [{ Name: 'N-NodeProp' }],
            //     //     },
            //     //     {
            //     //       name: 'Network-L',
            //     //       leftnode: 'Network',
            //     //       rightnode: 'Link',
            //     //       lprops: [{ Name: 'Network-L' }],
            //     //     },
            //     //     {
            //     //       name: 'Network-N',
            //     //       leftnode: 'Network',
            //     //       rightnode: 'Node',
            //     //       lprops: [{ Name: 'Network-N' }],
            //     //     },
            //     //     {
            //     //       name: 'Network-NetProp',
            //     //       leftnode: 'Network',
            //     //       rightnode: 'NetProp',
            //     //       lprops: [{ Name: 'Network-NetProp' }],
            //     //     },
            //     //     {
            //     //       name: 'NodeProp-LinkProp',
            //     //       leftnode: 'NodeProp',
            //     //       rightnode: 'LinkProp',
            //     //       lprops: [{ Name: 'NodeProp-LinkProp' }],
            //     //     },
            //     //   ],
            //     // })
            //     // console.log('after showw treeee')
          } catch (e) {
            console.error('ERROR:' + e)
            // console.log(e.stack)
          }

          function sortOn(property) {
            return function (a, b) {
              if (a[property] < b[property]) {
                return -1
              } else if (a[property] > b[property]) {
                return 1
              } else {
                return 0
              }
            }
          }

          function consoler(message) {
            //console.log(message)
          }
          /*
            parsedunet.network.nodes.sort(sortOn("name")); 
            x += '<table>'
            x += '<br>'
            x += "<header>Sort Nodes on Name</header>"
            x += "   <nav>"
            x += "      <ul>"
            parsedunet.network.nodes.forEach(function (item, index) {
                  x += '<li>Node:' + item.name + '</li>'
            })
            x += "      </ul>"
            x += "  </nav>"
            x += "<p>"
            x += "version:" + version
            */
          x += '</body>'
          x += '</html>'
          res.write(x)
          res.end('')
        } catch (e) {
          console.log(e)
        }
      }
    })
  } else {
    alert('no url found')
  }
})
server.listen(port) // listen for any incoming requests
console.log('work.js at port ' + port + ' is running..')
