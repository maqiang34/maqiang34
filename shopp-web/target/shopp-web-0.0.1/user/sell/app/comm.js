//if(!window.sessionStorage.asikey||parent.user==undefined){ parent.user=undefined;window.sessionStorage.clear();  window.top.location="../../login.html";}else{  user=parent.user;}
var  curr_time = new Date(), timefile, objtree,objTable,tablesize, stablesize,systoken=undefined,sys={imgrooturl:"http://123.206.197.208:8989/"};
var tool={
    col_getsl:function(){return new Date().getHours()%4+3},
    col_format:function(val){ if(val==null){val=new Date();} return new Date(val).Format("yyyy-MM-dd hh:mm:ss");},//格式化时间
    col_proformat:function(val){ if(val==null){return;} return new Date(val).Format("yyyy-MM-dd hh:mm:ss");},//格式化时间
    col_img:function(val){return ['<image  class="icon-tb" src=',sys.imgrooturl+val,' onclick=showimg(this)>'].join(""); },//格式化时间
    col_state:function(value ,row,index){return value=="1"?'<span class="icon-tb icon-online" ></span>':'<span class="icon-tb icon-offline" ></span>';},
    col_isdeal:function(value,row,index){return value=="1"?'<span class="icon-tb icon-online" title="是"></span>':'<span class="icon-tb icon-offline" title="否"></span>';},

    setimg : function(em, imgid, callback) {
		var oFile = $(em)[0].files[0];
		var rFilter = /^(image\/jpeg|image\/png|image\/gif|image\/bmp|image\/jpg)$/i;
		var msg = "*.gif,*.jpg,*.jpeg,*.png,*.bmp";
		if (!rFilter.test(oFile.type)) {
			layer.open({content : "格式错误~请选择格式为" + msg + "的图片~",btn : '确定'}); return;
		}else if(oFile.size > 10485760){
    		layer.open({content : "最大只能上传10M的图片",btn : '确定'}); return;
		}
		var oImage = document.getElementById(imgid);
		var oReader = new FileReader();
		oReader.onload = function(e) {oImage.src = e.target.result;};  oReader.readAsDataURL(oFile);
		if (callback != null) {callback();}
	},
};
function  alert_infomsg(msg){ $.messager.alert('提示', msg, 'info');}
function alert_errmsg(msg){ $.messager.alert('错误', msg, 'error');}
function msgShow(title, msgString, msgType) { $.messager.alert(title, msgString, msgType);}
function initTree(url,onSelect){objtree=$('#objtree').tree({url:url,method:'post',animate:true,lines:true, onSelect:onSelect});};
function reloaddata(){ objTable.datagrid("reload");};
function reloaddata(queryParams){objTable.datagrid( { queryParams:queryParams });};
function clearTable(){	objTable.datagrid("loadData",{ total:0,rows:[]});}
function getTableChecked(){ return objTable.datagrid('getChecked');}
function getTableCheckedID(){ var userID =[],checkedItems = objTable.datagrid('getChecked'); $.each(checkedItems, function (index, item) { userID.push(item.id); }); return userID;}
function showimg(em,url){$(em).viewer();}
function initTable(title,iconCls,method,url,queryParams,toptol,fottol,col,isautosize,onDblClickRow){
    if(isautosize){ tablesize= stablesize = parseInt((($("#objTable").height() -80) / 26));	}
    if(tablesize<10){tablesize=stablesize=10;}
     objTable=$('#objTable').datagrid({
      url:url,
      title:title,
      iconCls: iconCls,
      method:method,
      queryParams:queryParams,
      fit:true,
      fitColumns:true,
      remoteSort: false,
      striped:true,
      rownumbers:true,
      pagination:true,
      pageSize:stablesize,
      pageList:[tablesize,10,50,100,200,500],
      toolbar:toptol,
      columns:col,
      onLoadError:clearTable,
      onDblClickRow:onDblClickRow
   });
    if(fottol){
       objTable.datagrid().datagrid('getPager').pagination(fottol);	// get the pager of datagrid
   }
}


/**
 * 动态组件 无需关心
 */
//start===============================================================================================================================================================================================================================
function crspsh() {
    $('#div_st_filter').append("<div id=\"seache\"style=\"float:right;margin-right:20px;\"><input id=\"fddata\"class=\"easyui-searchbox\" val=\"ml\" data-options=\"prompt:'请输入搜索条件...',searcher:finddatatb\"style=\"width:300px;display:inline;\"></input><div id=\"mm\"style=\"width:100px\" ></div></div>");
    var muits = new Array();
    var fields = $('#objTable').datagrid('getColumnFields');
    for (var i = 0; i < fields.length; i++) {
        var opts = $('#objTable').datagrid('getColumnOption', fields[i]);
        if (opts.field == 'ck' ||opts.field == 'id' || opts.hidden || opts.field == 'hand'||opts.filter==false) {
            continue;
        }
        muits.push("<div id='" + fields[i] + "' name='" + fields[i] + "'  onclick='chclip(this);' data-options=\"iconCls:'icon-" + fields[i] + "'\">" + opts.title + "</div>");
    }
    $('#mm').html(String.prototype.concat.apply("", muits));
    $('#fddata').searchbox({menu: '#mm'});
//    $('#seache').appendTo('#div_st_filter');
} //简单查询
function finddatatb(value, name) {
    if (value.trim() == "" || name.trim() == "") {value=null,name=null; }
    objTable.datagrid('reload', {coleam: name, colval: value});
} //简单查找数据
function chclip(em) {
    $("#seache input[placeholder='请输入搜索条件...']").hide();
    $("#seache span[class='textbox combo']").remove();
    $("#scvlcc").remove();
    $("#seache input[class='textbox-value']").attr("value", "");
    $("#seache input[class='textbox-value']").attr("name", em.id);
    if (em.id == "isread" || em.id == "state") {
        var id = "#ch" + em.id;
        var magrlef = '94px';
        var width = '180px';
        var html = '<select id="scvlcc"  style="width: ' + width + ' ;height:18px;"></select>';
        $("#seache input[placeholder='请输入搜索条件...']").after(html);
        $('#scvlcc').combo({editable: false});
        $("#scvlcc").next().css({'margin-left': magrlef, 'margin-right': '18px', 'padding-bottom': '2px'});
        $(id).appendTo($('#scvlcc').combo('panel'));
        $(id + ' span').click(function () {
            $('#scvlcc').combo('setValue', $(this).attr("value")).combo('setText', $(this).text()).combo('hidePanel');
            $("#seache input[class='textbox-value']").attr("name", em.id);
            $("#seache input[class='textbox-value']").attr("value", $(this).attr("value"));
        });
        setTimeout(function () {
            $("span[class='textbox combo']").find("input[type='text']").css({'margin-left': '0px'});
        }, 0);
    } else {
        $("#seache input[placeholder='请输入搜索条件...']").show();
    }
}
//end===============================================================================================================================================================================================================================

function initimgs(oid,mt){
    $("#comp_imgList").empty();
    if(!oid){return;}
    $.ajax({ url: '/i/file/fileList.do',type: 'POST',data : {token:new Date().getHours()%4+3,mt:mt,oid:oid},   success: function(data) {
            if (data.status==200) { showimg('#comp_imgList',data.data,false,imginfo.oldimg);  }
        }
    });
}
function showimg(em,data,isadd,calldata){
    if(!isadd){  $(em).empty(); }
    if(data&&data.length>0){
        infoPicImg='';
        $.each(data,function(index,value){
            if(calldata){  calldata.push(value);}
            infoPicImg += "<div class='imgBox'><img id='old_Pic_" + value.id + "' data_id='"+value.id+"' data_value='"+value.location+"' class='imgBox' onclick='showimg(this)' src='"+sys.imgrooturl+value.location+"'><i class='imgClose' onclick='deleteImg(" + value.id + ",0)'>&times;</i></div>";
        });
        $(em).append(infoPicImg);
    }
}

/*图片选中*/
function picChange(e, flag) {
    var files = e.files;
    if(files.length==0){return}
    if(imginfo.oldimg.length+imginfo.addimg.length+files.length>10){ alert_infomsg("最多只能上传十张！请重新选择"); }
//    imginfo.i= imginfo.i+files.length;
    for (var i = 0; i < files.length; i++) {
        imginfo.i++;
        files[i].id=imginfo.i ;
        imginfo.addimg.push(files[i]);
        //--------------ADD----------------
        $("#comp_imgList").append( "<div class='imgBox'><img id='new_Pic_" + (files[i].id) + "' class='imgBox' onclick='showimg(this)' src=''><i class='imgClose' onclick='deleteImg(" + (files[i].id) + ",1)'>&times;</i></div>");

        var reader = new FileReader();
        reader.id=files[i].id;
        reader.readAsDataURL(files[i]);
        reader.onload = function (theFile) {
           $("#new_Pic_"+this.id).attr("src",this.result)
        }
    }
}

/*图片删除*/
function deleteImg(imgid, flag) {
    if(flag==0){//已上传到
        $("#old_Pic_"+imgid).parent().remove();
        $.each( imginfo.oldimg,function(index,value){
            if(value.id==imgid){
                imginfo.delimg.push(value.location);
                imginfo.delimgid.push(value.id);
                imginfo.oldimg.splice(index,1)
                return;
            }
        });
    }else{//新加的
        $("#new_Pic_"+imgid).parent().remove();
        $.each( imginfo.addimg,function(index,value){
            if(value.id==imgid){
                imginfo.addimg.splice(index,1);
                return;
            }
        });
    }
}

//end===============================================================================================================================================================================================================================
/*
* 扩展校验规则
* */
$.extend($.fn.validatebox.defaults.rules, {
    idcard: {// 验证身份证
        validator: function (value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message: '身份证号码格式不正确'
    },
    minLength: {
        validator: function (value, param) {
            return value.length >= param[0];
        },
        message: '请输入至少{0}个字符.'
    },
    length: { validator: function (value, param) {
        var len = $.trim(value).length;
        return len >= param[0] && len <= param[1];
    },
        message: "输入内容长度必须介于{0}和{1}之间."
    },
    phone: {// 验证电话号码
        validator: function (value) {
            return /^((\d2,3 )|(\d{3}\-))?(0\d2,3 |0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '格式不正确,请使用下面格式:020-88888888'
    },
    mobile: {// 验证手机号码
        validator: function (value) {
            return /^(13|14|15|17|18)\d{9}$/i.test(value);
        },
        message: '手机号码格式不正确'
    },
    intOrFloat: {// 验证整数或小数
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '请输入数字，并确保格式正确'
    },
    currency: {// 验证货币
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '货币格式不正确'
    },
    qq: {// 验证QQ,从10000开始
        validator: function (value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message: 'QQ号码格式不正确'
    },
    integer: {// 验证整数 可正负数
        validator: function (value) {
            //return /^[+]?[1-9]+\d*$/i.test(value);


            return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
        },
        message: '请输入整数'
    },
    age: {// 验证年龄
        validator: function (value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message: '年龄必须是0到120之间的整数'
    },
    chinese: {// 验证中文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message: '请输入中文'
    },
    english: {// 验证英语
        validator: function (value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message: '请输入英文'
    },
    unnormal: {// 验证是否包含空格和非法字符
        validator: function (value) {
            return /.+/i.test(value);
        },
        message: '输入值不能为空和包含其他非法字符'
    },
    username: {// 验证用户名
        validator: function (value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno: {// 验证传真
        validator: function (value) {
            //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
            return /^((\d2,3 )|(\d{3}\-))?(0\d2,3 |0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '传真号码不正确'
    },
    zip: {// 验证邮政编码
        validator: function (value) {
            return /^[1-9]\d{5}$/i.test(value);
        },
        message: '邮政编码格式不正确'
    },
    ip: {// 验证IP地址
        validator: function (value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message: 'IP地址格式不正确'
    },
    name: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
        },
        message: '请输入姓名'
    },
    date: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            //格式yyyy-MM-dd或yyyy-M-d
            return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
        },
        message: '清输入合适的日期格式'
    },
    msn: {
        validator: function (value) {
            return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    pwd: {
        validator: function (value) {

            return /^\w{6,17}$/.test(value);
        },
        message: '正确格式为：长度在6-18之间，只能包含字符、数字和下划线'
    },
    same: {
        validator: function (value, param) {
            if ($("#" + param[0]).val() != "" && value != "") {
                return $("#" + param[0]).val() == value;
            } else {
                return true;
            }
        },
        message: '两次输入的密码不一致！'
    },
    ckdevno: {
    	 validator: function (value) {
          return value.length==6||value.length==8;
         },
         message: '设备编号不正确！设备编码长度为6或者8位!'
    },
    cksystoken: {
   	 validator: function (value) {
         return value.length==32&&value==systoken;
        },
        message: '口令不正确！'
   }
});

//