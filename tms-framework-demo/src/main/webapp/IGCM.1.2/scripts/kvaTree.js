(function ($){
	//����Tree
    $.fn.kvaTree = function (options) {
        return $(this).each(function () {
            var binded = false;
            var tree   = $(this);
            var kt     = this;
            var ddover = null;
            
            kt.options = {
                autoclose: true,
                background: 'white',
                imgFolder: 'img/',
                overrideEvents: false,
                multi: true,
                dragdrop: true,
                onClick: false,
                onDblClick: false,
                onExpand: false,
                onCollapse: false,
                onAddNode: false,
                onEditNode: false,
                onDeleteNode: false,
                onDrag: false,
                onDrop: false
            };
            
            kt.opts = $.extend({},kt.options,options);
            
            //��ʼ��Tree
            $.fn.kvaTree.InitKvaTree = function (obj)
            {
                tree = $(obj);
                
                var sep = '<li class="separator"></li>';
            
                tree.find('li:not(.separator)').filter(function () {
                    if (($(this).prev('li.separator').get(0)) || ($(this).parents('ul.non-kvaTree').get(0)))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                })
                .each(function () {
                    $(this).before(sep);
                });
            
                tree.find('li > span').not('.sign').not('.clr').addClass('text').attr('unselectable','on');
            
                tree.find('li:not(.separator)').
                filter(function () {
                    if ($(this).parents('ul.non-kvaTree').get(0))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                })
                .filter(':has(ul)').addClass('node')
                .end()
                .filter(':not(.node)').addClass('leaf');
                
                kt.IeSetStyles();
                
                kt.Clean();
        
                kt.AddSigns();
                
                if (!binded)
                {
                    kt.BindEvents(obj);
                }
                
                //dragdrop
                if (kt.opts.dragdrop)
                {
                    kt.CancelDragDrop(obj);
                
                    kt.InitDragDrop(obj);
                }//if (kt.opts.dragdrop)
            };//InitKvaTree
            
            //��ʼ����ק�¼�
            kt.InitDragDrop = function (obj)
            {
                tree = $(obj);
                
                tree.find('span.text').draggable({
                    cursor: ($.browser.msie)? 'default' : 'move',
                    helper: function () {
                        return $('<div id="kvaTree-drag"><span>' + $(this).text() + '</span></div>');
                    },
                    appendTo: tree
                });
                
                tree.find('li.separator').droppable({
                    accept: 'span.text',
                    hoverClass: 'dd-hover'
                });
                
                tree.find('span.text').bind('dragstart',function (event,ui) {
                    tree = $(this).parents('ul.kvaTree:first');
                
                    var li = $(this).parent('li');
                    var dd = $('div#kvaTree-drag');
                
                    if ($.browser.msie)
                    {
                        tree.find('li.separator').removeClass('dd-hover');
                    }
                    
                    if ($.browser.opera)
                    {
                        dd.css('margin-top','10px');
                    }
                
                    if (li.is('.leaf'))
                    {
                        dd.addClass('leaf');
                        
                        if ($.browser.msie)
                        {
                            dd.css('background','#C3E1FF url(' + kt.opts.imgFolder + 'leaf-drag.gif) left 3px no-repeat');
                        }
                    }//if (li.is('.leaf'))
                    else if (li.is('.node'))
                    {
                        dd.addClass('node');
                    }
                    
                    li.prev('li.separator').addClass('alt').end().addClass('alt');
                    
                    if (typeof(kt.opts.onDrag) == 'function')
                    {
                        kt.opts.onDrag(event,li);
                    }
                });

                tree.find('li.separator').bind('dropover',function (event,ui) {
                    ddover = $(this);
                });
                
                tree.find('li.separator').bind('dropout',function (event,ui) {
                    ddover = null;
                });
                
                tree.find('span.text').bind('dragstop',function (event,ui) {
                    var lvlok = true;
                
                    if (ddover)
                    {
                        var ali = tree.find('li.alt:not(.separator):eq(0)');
                        var hli = ddover.parents('li.node:eq(0)');
                        
                        if ((ali.is('.node')) && (hli.is('.fixedLevel')))
                        {
                            lvlok = false;
                        }
                    }//if (ddover)
                        
                    if ((ddover) && (lvlok))
                    {
                        ddover.before(tree.find('li.alt').remove().removeClass('alt'));
                        
                        ddover = null;
                        
                        if (typeof(kt.opts.onDrop) == 'function')
                        {
                            kt.opts.onDrop(event,ali);
                        }
                        
                        $.fn.kvaTree.InitKvaTree(obj);
                    }//if (ddover)
                    else
                    {
                        tree.find('li.alt').removeClass('alt');
                    }
                });
            };//InitDragDrop
            
            //�����ק�¼�
            kt.CancelDragDrop = function (obj)
            {
                tree = $(obj);
            
                tree.find('span.text').draggable('destroy');
                tree.find('li.separator').droppable('destroy');
                tree.find('li.separator').unbind();
                tree.find('span.text').unbind();
            };//CancelDragDrop
            
            //��ӽڵ�
            $.fn.kvaTree.AddNode = function (obj,type)
            {
                tree = $(obj);
                
                var actel = tree.find('span.active').get(0);
                
                if (actel)
                {
                    var li    = $(actel).parents('li:first');
                    var lin   = $(actel).parents('li.node:first');
                
                    if ((!lin.is('.fixedLevel')) || (type != 'node'))
                    {
                        var cn    = (type == 'leaf')? '' : ' class="node"';
                    
                        var sep   = '<li class="separator"></li>';
                        var nli   = '<li' + cn + '><span class="text">&nbsp;</span><input type="text" value="New item" /></li>';
                        var ncont = sep + nli;
                        
                        var ok    = false;
                        
                        if (li.is('.leaf'))
                        {
                            li.after(ncont);
                            
                            var node = li.nextAll('li:not(.separator):first');
                            
                            var iprnt = li.parent();
                            
                            ok        = true;
                        }//if (li.is('.leaf'))
                        else if (li.is('.node'))
                        {
                            var childul = li.children('ul').get(0);
                        
                            if (childul)
                            {
                                $(childul).append(ncont);
                                
                                var node = $(childul).children('li:not(.separator):last');
                            }//if (childul)
                            else
                            {
                                li.append('<ul>' + ncont + '</ul>');
                                
                                var childul = li.children('ul').get(0);
                                
                                var node = $(childul).children('li:not(.separator):last');
                            }//else
                            
                            kt.ExpandNode(obj,li);
                            
                            var iprnt = li;
                            
                            ok        = true;
                        }//else if ( ...
                        
                        if (ok)
                        {
                            $(actel).removeClass('active');
                        
                            iprnt.find('input:text').focus().select().blur(function () {
                                kt.SaveInput(obj,$(this));
                            });
                        }//if (ok)
                        
                        $.fn.kvaTree.InitKvaTree(obj);
                        
                        if (typeof(kt.opts.onAddNode) == 'function')
                        {
                            kt.opts.onAddNode(node);
                        }
                    }//if ((!li.is('.fixedLevel')) || (type != 'node'))
                }//if (actel)
            };//AddNode
            
            //�༭�ڵ�
            $.fn.kvaTree.EditNode = function (obj)
            {
                tree = $(obj);
                
                var actel = tree.find('span.active').get(0);
                
                if (actel)
                {
                    var li = $(actel).parents('li:first');
                    
                    $(actel).replaceWith('<span class="text">&nbsp;</span><input type="text" value="' + $(actel).text() + '" />');
                    
                    li.find('input:text').focus().select().blur(function () {
                        kt.SaveInput(obj,$(this));
                    });
                    
                    if (typeof(kt.opts.onEditNode) == 'function')
                    {
                        kt.opts.onEditNode(li);
                    }
                }//if (actel)
            };//EditNode
            
            //ɾ���ڵ�
            $.fn.kvaTree.DeleteNode = function (obj)
            {
                tree = $(obj);
                
                var actel = tree.find('span.active').get(0);
                
                if (actel)
                {
                    var li   = $(actel).parents('li:first');
                    var prnt = li.parents('li.node:first');
                    
                    li.prev('li.separator').remove().end().remove();
                    
                    $.fn.kvaTree.InitKvaTree(obj);
                    
                    if (typeof(kt.opts.onDeleteNode) == 'function')
                    {
                        kt.opts.onDeleteNode(li,prnt);
                    }
                }//if (actel)
            };//DeleteNode
            
            //����ڵ�����
            kt.SaveInput = function (obj,input)
            {
                input.prev('span.text').remove();
            
                var val = ($.trim(input.get(0).value) != '')? input.get(0).value : '_____';
            
                input.replaceWith('<span class="active text">' + val + '</span>');
                
                $.fn.kvaTree.InitKvaTree(obj);
            };//SaveInput
            
            //��ʼ��ҳ��ʱ�жϽڵ��Ƿ�Ĭ��չ�������÷��� class="open"
            kt.IeSetStyles = function ()
            {
                if ($.browser.msie)
                {
                    tree.find('li.node:not(.open) > ul').hide();
                    tree.find('li.node.open > ul').css('margin-bottom','1px');
                }//if ($.browser.msie)
            };//IeSetStyles
            
            //�ڵ������߱���
            kt.Clean = function ()
            {
                tree.find('li:not(.separator)').each(function () {
                    var show = false;
                
                    if ($(this).next('li').get(0))
                    {
                        var bg = 'url(' + kt.opts.imgFolder + 'line-vertical.gif) left top repeat-y';
                        
                        show   = true;
                    }//if ($(this).next('li').get(0))
                    else if ((!$(this).next('li').get(0)) || ($(this).find('ul').get(0)))
                    {
                        var bg = kt.opts.background;
                        
                        show   = true;
                    }//else if
                    
                    if (show)
                    {
                        $(this).find('span.clr').remove();
                    
                        var height = $(this).height();
                        
                        var top    = ($(this).is('.node'))? 12 : 8;
                        
                        $(this).append('<span class="clr" style="width: 1px; height: ' + height + 'px; position: absolute; left: 0; top: ' + top + 'px; background: ' + bg + ';"></span>');
                    }//if (show)
                });
            };//Clean
            
            //�ڵ�չ��������ʱ��ӱ��
            kt.AddSigns = function ()
            {
                tree.find('li.node').each(function () {
                    if ($(this).hasClass('open'))
                    {
                        $(this).find('span.sign').remove().end().append('<span class="sign minus"></span>');
                    }//if ($(this).hasClass('open'))
                    else
                    {
                        $(this).find('span.sign').remove().end().append('<span class="sign plus"></span>');
                    }//else
                });
            };//AddSigns
            
            //������Tree�¼�
            kt.BindEvents = function (obj)
            {
                tree.click(function (e) {
                    var clicked = $(e.target);
                    
                    if (clicked.is('span.sign'))
                    {
                        var node = clicked.parents('li:eq(0)');
                    
                        kt.ToggleNode(obj,node);
                    }//if (clicked.is('span.sign'))
                    else if (clicked.is('span.text'))
                    {
                        var node = clicked.parents('li:eq(0)');
                        
                        if (typeof(kt.opts.onClick) == 'function')
                        {
                            if (!kt.opts.overrideEvents)
                            {
                                tree.find('.active').removeClass('active');
                            
                                clicked.addClass('active');
                            }//if (!opts.overrideEvents)
                        
                            kt.opts.onClick(e,node);
                        }
                        else
                        {
                            tree.find('.active').removeClass('active');
                            
                            clicked.addClass('active');
                        }//else
                    }//if (clicked.is('span.text'))
                });
                
                tree.dblclick(function (e) {
                    var clicked = $(e.target);
                    
                    if (clicked.is('span.text'))
                    {
                        var node = clicked.parents('li:eq(0)');
                        
                        if (typeof(kt.opts.onDblClick) == 'function')
                        {
                            if ((!kt.opts.overrideEvents) && (node.is('.node')))
                            {
                                kt.ToggleNode(obj,node);
                            }//if (!opts.overrideEvents)
                        
                            kt.opts.onDblClick(e,node);
                        }
                        else if (node.is('.node'))
                        {
                            kt.ToggleNode(obj,node);
                        }//else
                    }//if (clicked.is('span.text'))
                });
                
                binded = true;
            };//BindEvents
            
            
            //Toggle�������չ���ڵ�
            kt.ToggleNode = function (obj,node)
            {
            		
                if (node.hasClass('open'))
                {
                    kt.CollapseNode(node);
                }
                else
                {
                    kt.ExpandNode(obj,node);
                }
                
                kt.Clean();
            };//ToggleNode 
            
            //չ���ڵ�
            kt.ExpandNode = function (obj,node)
            {
                node.addClass('open');
                
                if (kt.opts.autoclose)
                {
                    node.siblings('.open').each(function () {
                        kt.CollapseNode($(this));
                    });
                }//if (opts.autoclose)
                
                if ($.browser.msie)
                {
                    node.children('ul').show().css({
                        'margin-bottom': '1px',
                        'visibility': 'visible'
                    });
                    
                    node.children('ul').find('li.node:not(.open) > ul').each(function () {
                        $(this).css('visibility','hidden');
                    });
                }//if ($.browser.msie)
                
                var sign = node.find('span.sign:last');
                
                sign.removeClass('plus').addClass('minus');
                
                if (kt.opts.multi)
                {
                    $.fn.kvaTree.InitKvaTree(obj);
                }
                
                if (typeof(kt.opts.onExpand) == 'function')
                {
                    kt.opts.onExpand(node);
                }
            };//ExpandNode
            
            //����ڵ�
            kt.CollapseNode = function (node)
            {
                node.removeClass('open');
                    
                if ($.browser.msie)
                {
                    node.children('ul').hide();
                }
                
                var sign = node.find('span.sign:last');
                
                sign.removeClass('minus').addClass('plus');
                
                if (typeof(kt.opts.onCollapse) == 'function')
                {
                    kt.opts.onCollapse(node);
                }
            };//CollapseNode
            
            if ($(this).is('ul'))
            {
                var tree = $(this);
            
                tree.addClass('kvaTree');
                
                $.fn.kvaTree.InitKvaTree(kt);
            }//if ($(this).is('ul'))
        });
    };
})(jQuery);
