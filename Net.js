'use strict'
/* Net.ts - universal network from NetTree.net
   ToDos: how to update a node or link? Maybe delete and add back
   change leftnode, rightnode to left and right
   add some error throwing
   this.version = "20220917 1111 working on NetTree/showTree";
   this.version = "20221028 1111 added default"
*/
//exports.__esModule = true;

// exports.Prop = exports.Link = exports.Node = exports.Network = void 0
const version = '20221028 1111 added default'
export var Network = /** @class */ (function () {
  function Network(name) {
    this.version = '20221028 1111 added default'
    this.name = name
    this.props = new Map()
    this.nodes = new Map()
    this.links = new Map()

    console.log('this.nodes', this.nodes)
    console.log('this.links', this.links)
  }
  Network.prototype.addProp = function (keyI, valueI) {
    console.log('keyIkeyI', keyI)

    if (this.props.has(keyI)) {
      console.log('Net:aP:PropKey ' + keyI + ' exists.')
    } else {
      this.props.set(keyI, new Prop(keyI, valueI))
      console.log(
        'this.props.set(keyI, new Prop(keyI, valueI))',
        this.props.set(keyI, new Prop(keyI, valueI))
      )
      console.log('this.props', this.props)
      return this.props.get(keyI)
    }
  }
  Network.prototype.getProp = function (keyI) {
    return this.props.get(keyI)
  }
  Network.prototype.getProps = function () {
    return this.props
  }
  Network.prototype.hasProp = function (key) {
    if (this.props.has(key)) {
      return true
    } else {
      return false
    }
  }
  Network.prototype.getNumNodes = function () {
    return this.nodes.size
  }
  Network.prototype.getNumLinks = function () {
    return this.links.size
  }
  Network.prototype.addNode = function (name) {
    var nodehold = new Node(name)
    if (this.nodes.has(name)) {
      console.log('Node exists.')
    } else {
      this.nodes.set(name, nodehold)
      return nodehold
    }
  }
  Network.prototype.addNodeObj = function (nodeI) {
    this.nodes.set(nodeI.name, nodeI)
  }
  Network.prototype.hasNode = function (name) {
    return this.nodes.has(name)
  }
  Network.prototype.getNode = function (name) {
    if (this.hasNode(name)) {
      return this.nodes.get(name)
    } else {
      console.log('Net:gN:Node ' + name + '  does not exist.')
    }
  }
  Network.prototype.deleteNode = function (name) {
    if (this.nodes.has(name)) {
      this.nodes['delete'](name)
      return true
    } else {
      console.log('Net:dN:Node ' + name + ' does not exist.')
      return false
    }
  }
  Network.prototype.hasLink = function (name) {
    return this.links.has(name)
  }
  Network.prototype.addLink = function (name, leftnode, rightnode) {
    var linkhold = new Link(name, leftnode, rightnode)
    if (this.links.has(name)) {
      console.log('Nets:aL:Link ' + name + ' already exists.')
    } else {
      this.links.set(name, linkhold)
      return linkhold
    }
  }
  Network.prototype.addLinkObj = function (linkI) {
    this.links.set(linkI.name, linkI)
  }
  Network.prototype.getLink = function (name) {
    if (this.hasLink(name)) {
      return this.links.get(name)
    } else {
      console.log('Nets:gL:Link ' + name + ' does not exist.')
    }
  }
  Network.prototype.deleteLink = function (name) {
    if (this.links.has(name)) {
      this.links['delete'](name)
      return true
    } else {
      console.log('Nets:dL:Link ' + name + ' does not exist.')
      return false
    }
  }
  return Network
})()
// exports.Network = Network;

export var Node = /** @class */ (function () {
  function Node(name) {
    this.name = name

    this.props = new Map()
    console.log('this.props in Node', this.props)
    return this
  }
  Node.prototype.addProp = function (key, value) {
    if (this.props.has(key)) {
      console.log('N:aP:PropKey ' + key + ' exists.')
    } else {
      this.props.set(key, new Prop(key, value))
      console.log(
        '123this.props.set(key, new Prop(key, value))',
        this.props.set(key, new Prop(key, value))
      )
      return this.props.get(key)
    }
  }
  Node.prototype.getProp = function (key) {
    return this.props.get(key)
  }
  Node.prototype.getProps = function () {
    return this.props
  }
  Node.prototype.hasProp = function (key) {
    if (this.props.has(key)) {
      return true
    } else {
      return false
    }
  }
  Node.prototype.deleteProp = function (name) {
    if (this.props.has(name)) {
      this.props['delete'](name)
      return true
    } else {
      console.log('Nets:N:dP:nprop ' + name + ' does not exist.')
      return false
    }
  }
  return Node
})()
// exports.Node = Node;

export var Link = /** @class */ (function () {
  function Link(name, leftnode, rightnode) {
    console.log('leftnodeleftnode', leftnode)
    console.log('rightnoderightnode', rightnode)

    this.name = name
    this.leftnode = leftnode
    this.rightnode = rightnode
    this.props = new Map()
    return this
  }
  Link.prototype.addProp = function (key, value) {
    if (this.props.has(key)) {
      console.log('L:aP:PropKey ' + key + ' exists.')
    } else {
      this.props.set(key, new Prop(key, value))
      return this.props.get(key)
    }
  }
  Link.prototype.getProp = function (key) {
    return this.props.get(key)
  }
  Link.prototype.getProps = function () {
    return this.props
  }
  Link.prototype.hasProp = function (key) {
    if (this.props.has(key)) {
      return true
    } else {
      return false
    }
  }
  Link.prototype.deleteProp = function (name) {
    if (this.props.has(name)) {
      this.props['delete'](name)
      return true
    } else {
      console.log('Nets:L:dP:nprop ' + name + ' does not exist.')
      return false
    }
  }
  return Link
})()
// exports.Link = Link;

export var Prop = /** @class */ (function () {
  function Prop(key, value) {
    this.key = key
    this.value = value
    return this
  }
  return Prop
})()
// exports.Prop = Prop;

export default function ver() {
  return version
}
/*
function default() {
return version
}
*/
//module.exports = { Network: Network, Prop: Prop, Node: Node, Link: Link, ver: ver, default: default};
