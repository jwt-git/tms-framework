// JavaScript Document

function _(id) { 
  return document.getElementById(id); 
} 
var ti = null; 
function loading(){ 
    var tmd = 0; 
    var x1 = document.documentElement.clientWidth;  
    var y1 = document.body.offsetHeight; 
    var y2=document.documentElement.clientHeight;//整个页面的高度 
    with(_("layer")){ 
       style.width=x1+"px"; 
       if(y2>y1){ 
       style.height=y2+"px"; 
       }else{ 
       style.height=y1+"px"; 
       } 
       style.overflowX="hidden"; 
       style.overflowY="hidden"; 
       style.visibility="visible"; 
    } 
    _("layer").style.left=0; 
    _("layer").style.filter='Alpha(Opacity=0)'; 
    document.body.style.overflowX="hidden"; 
    document.body.style.overflowY="hidden"; 
    _("layer2").style.top=parseInt(document.documentElement.scrollTop)+((document.documentElement.clientHeight-250)/2)+"px";  
    _("layer2").style.left="50%"; 
    _("layer2").style.marginLeft="-250px" 
    _("layer2").style.visibility="visible"; 
    ti = setInterval("hei()",10); 
} 
var x = 0; 
function hei(){ 
    x+=10; 
    if(x<31){ 
        if(document.all){ 
            _("layer").style.filter='Alpha(Opacity='+x+')'; 
        }else{ 
            _("layer").style.opacity=""+x/100+"";     
        } 
    } 
} 
function unload(){ 
_("layer").style.visibility="hidden"; 
_("layer2").style.visibility="hidden"; 
clearInterval(ti); 
x=0; 
} 