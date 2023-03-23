// parsedunet2net Parsed .unet to Net
/*

var version = '20220914 1332 original pulled from works.'
var version = '20221206 0941 uncommented default ver.'
*/
// import Net from './Net.js'
const Net = require('./Net.js')
//const { Console } = require('console');
//import Console from 'console'
var version = '20221206 0941 uncommented default ver.'
var np
var nodeA = null
var linkA = null

//export default function parsedunet2net(parsedunet) {
function parsedunet2net(parsedunet) {
    //var netnetwork = new Net.Network(parsedunet.network.name);
    const netnetwork = new Net.Network(parsedunet.network.name);
    console.log('p2n:netnetwork.name:' +  netnetwork.name)
    for (var netp = 0; netp < parsedunet.network.netprops.length; netp++) {
        for (const [key, value]
            of Object.entries(parsedunet.network.netprops[netp])) {
            //console.log('key:' + key + ' value:' + value)
            netnetwork.addProp(key, new Net.Prop(key, value));
            //netnetwork.addProp(key, value);
        }
    }
    for (var n = 0; n < parsedunet.network.nodes.length; n++) {
        nodeA = new Net.Node(parsedunet.network.nodes[n].name);
        netnetwork.nodes.set(nodeA.name, nodeA);
        for (np = 0; np < parsedunet.network.nodes[n].nprops.length; np++) {
            for (const [key, value]
                of Object.entries(parsedunet.network.nodes[n].nprops[np])) {
                nodeA.addProp(key, value)
            }
        }
    }
    for (var l = 0; l < parsedunet.network.links.length; l++) {
        linkA = new Net.Link(parsedunet.network.links[l].name,
            new Net.Node(parsedunet.network.links[l].leftnode),
            new Net.Node(parsedunet.network.links[l].rightnode));
        netnetwork.links.set(linkA.name, linkA);
        for (var lp = 0; lp < parsedunet.network.links[l].lprops.length; lp++) {
            for (const [key, value]
                of Object.entries(parsedunet.network.links[l].lprops[lp])) {
                linkA.addProp(key, value)
            }
        }
    }
    
    console.log('p2n:netnetwork.getNumNodes():' +  netnetwork.getNumNodes())
    /*
    netnetwork.nodes.forEach(element => {               console.log(element)            });
    netnetwork.links.forEach(element => {               console.log(element)            });
    netnetwork.props.forEach(element => {               console.log(element)            })
    */
    return netnetwork
}


export default function ver() {
   return version
}

module.exports = {parsedunet2net}
