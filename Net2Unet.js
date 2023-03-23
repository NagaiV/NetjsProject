/*
Net2Unet.js
purpose to write a .unet file given a Net.js netjs network
This will be a test fixture and will be incorporated into netlistener
version: 20220308 rbg started with unet.js

How to get a Network in for writing.
*/

'use strict'

const version = '202203091550'
import fs from 'fs'
import http from 'http'
import url from 'url'
// const fs = require('fs')
// const http = require('http')
// const url = require('url')

import querystring from 'querystring'
// const querystring = require('querystring')
import request from 'request'
import { Network, Node, Link, Prop } from './Net.js'
// import dll from './dll.js'
// var request = require('request')
// const Net = require('./Net.js')
// const dll = require('./dll.js')
import { Console } from 'console'
import { conditionalExpression } from '@babel/types'
// const { Console } = require('console')
// const { conditionalExpression } = require('@babel/types')

function consoler(message) {
  console.log(message)
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

var port = 3030
var server = http.createServer(function (req, res) {
  const queryObject = url.parse(req.url, true).query
  consoler(queryObject)
  var jo = querystring.parse(querystring.stringify(queryObject))
  //for (let [key, value] of ObjectConstructor.entries(jo)) {
  // res.write('key/value:' + `${key}/${value}` + `\n<br/>`);
  //}
  request({ uri: jo.url }, function (error, response, body) {
    if (typeof body !== 'undefined') {
      try {
        var nettree = JSON.parse(String(body))
        //consoler('network.name:' + nettree.network.name)
        consoler('Net2Unet.js:version:' + version + '\n')
        const netnetwork = new Network(nettree.network.name)
        consoler('Net.js:version:' + netnetwork.version + '\n')
        for (var netp = 0; netp < nettree.network.netprops.length; netp++) {
          for (const [key, value] of Object.entries(
            nettree.network.netprops[netp]
          )) {
            //consoler('key/val:' + key + '/' + value)
            netnetwork.addProp(key, value)
          }
        }
        for (var n = 0; n < nettree.network.nodes.length; n++) {
          nodeA = new Node(nettree.network.nodes[n].name)
          netnetwork.nodes.set(nodeA.name, nodeA)
          for (np = 0; np < nettree.network.nodes[n].nprops.length; np++) {
            for (const [key, value] of Object.entries(
              nettree.network.nodes[n].nprops[np]
            )) {
              nodeA.addProp(key, new Prop(key, value))
            }
          }
        }
        for (var l = 0; l < nettree.network.links.length; l++) {
          linkA = new Link(
            nettree.network.links[l].name,
            new Node(nettree.network.links[l].leftnode),
            new Node(nettree.network.links[l].rightnode)
          )
          netnetwork.links.set(linkA.name, linkA)
          for (var lp = 0; lp < nettree.network.links[l].lprops.length; lp++) {
            for (const [key, value] of Object.entries(
              nettree.network.links[l].lprops[lp]
            )) {
              linkA.addProp(key, new Prop(key, value))
            }
          }
        }

        consoler('{' + '\n')
        consoler('"network": {\n')
        consoler(lev(1) + '"name": "' + netnetwork.name + '",' + '\n')
        var count = netnetwork.props.size
        var counter = 0
        consoler(lev(1) + '"netprops":[' + '\n')
        consoler(lev(2) + '{' + '\n')
        netnetwork.props.forEach(function (item, index) {
          counter++
          consoler(lev(3) + '"' + index + '": "' + item + '"')
          if (counter === count) {
            consoler(' ' + '\n')
          } else {
            consoler(' ,' + '\n')
          }
        })
        consoler(lev(2) + '}' + '\n')
        consoler(lev(1) + '],' + '\n')
        consoler(lev(1) + '"Nodes":[' + '\n')
        var nodeiter = netnetwork.nodes.values
        var nodecount = netnetwork.nodes.size
        var nodecounter = 0
        netnetwork.nodes.forEach(function (node, nkey) {
          nodecounter++
          consoler(lev(2) + '{' + '\n')
          consoler(lev(3) + '"name": "' + node.name + '",' + '\n')
          let nprophold = null
          const npropiter = node.props.values()
          var npropcount = node.props.size
          var npropcounter = 0
          consoler(lev(3) + '"nprops":[' + '\n')
          consoler(lev(4) + '{' + '\n')
          while ((nprophold = npropiter.next().value)) {
            npropcounter++
            consoler(
              lev(5) + '"' + nprophold.key + '": "' + nprophold.value + '"'
            )
            if (npropcounter === npropcount) {
              consoler('' + '\n')
            } else {
              consoler(',' + '\n')
            }
          }
          consoler(lev(4) + '}' + '\n')
          consoler(lev(3) + ']' + '\n')
          consoler(lev(2) + '}')
          if (nodecounter === nodecount) {
            consoler('' + '\n')
          } else {
            consoler(',' + '\n')
          }
        })
        consoler(lev(1) + '],' + '\n')
        consoler(lev(1) + '"Links":[' + '\n')
        var linkiter = netnetwork.links.values
        var linkcount = netnetwork.links.size
        var linkcounter = 0
        netnetwork.links.forEach(function (link, lkey) {
          linkcounter++
          consoler(lev(2) + '{' + '\n')
          consoler(
            lev(3) +
              '"name": "' +
              link.name +
              '", ' +
              '"leftnode": "' +
              link.leftnode +
              '", ' +
              '"node": "' +
              link.rightnode +
              '",' +
              '\n'
          )
          let prophold = null
          consoler(lev(3) + '"lprops":[' + '\n')
          consoler(lev(4) + '{' + '\n')
          const propiter = link.props.values()
          var count = link.props.size
          var counter = 0
          while ((prophold = propiter.next().value)) {
            counter++
            consoler(
              lev(5) + '"' + prophold.key + '": "' + prophold.value + '"'
            )
            if (counter === count) {
              consoler('' + '\n')
            } else {
              consoler(',' + '\n')
            }
          }
          consoler(lev(4) + '}' + '\n')
          consoler(lev(3) + ']' + '\n')
          consoler(lev(2) + '}')

          if (linkcounter === linkcount) {
            consoler('' + '\n')
          } else {
            consoler(',' + '\n')
          }
        })
        consoler(lev(1) + ']' + '\n')
        consoler('}' + '\n')
        consoler('}' + '\n')

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
          process.stdout.write(message)
        }

        function lev(cnt) {
          var str = ''
          for (let i = 1; i <= cnt; i++) {
            str += '  '
          }
          return str
        }
      } catch (e) {
        consoler(e)
      }
    }
  })
})
server.listen(port) // listen for any incoming requests
console.log('Net2Unet.js ver ' + version + ' at port ' + port + ' is running..')
