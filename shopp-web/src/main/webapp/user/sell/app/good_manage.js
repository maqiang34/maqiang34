var queryParams = {page: null, rows: null,  keyword: null},imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};
var gt_list=['','水果' ,'蔬菜' , '坚果' , '畜禽','食品'],gd_lave=['','精品','特优','一级','二级','精选' ],
    good_type=[ {id:  1	,text:'水果'}, {id: 2,text:'蔬菜'},{id: 3	,text:'坚果'},{id: 4,text:'畜禽'}, {id: 5,text:'食品'}],
   good_lave=[{id:  1	,text:'精品'}, {id: 2,text:'特优'},{id: 3	,text:'一级'},{id: 4,text:'二级'}, {id: 5,text:'精选'}],
   good_unit=[{id:  1	,text:'g'}, {id: 2,text:'500g'},{id: 3	,text:'kg'},{id: 4,text:'袋'}, {id: 5,text:'箱'}];

//======================================================================格式化col=======================================================

function col_goodtype(value,row,index){ return gt_list[value]};
function col_goodlave(value,row,index){ return gd_lave[value]};

//======================================================================菜单事件function=======================================================
function initComp(add){
    imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};
    $("#comp_imgList").empty();
    $("#img_logo").attr("src","");
    $('#from_data').form('clear');
    $('#sl_compid').combobox({ url:'/i/company/allList.do', valueField:'id', textField:'text', onSelect: function(rec){
            var url = '/i/grow/allList.do?cid='+rec.id;
            $('#sl_growid').combobox('reload', url);
        }});
      if(add){ $("#sl_compid,#sl_growid").combobox("enable"); }else{  $("#sl_compid,#sl_growid").combobox("disable");}
}
//工具栏--》删除
var tl_deleteData = function () {
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        $.messager.confirm('删除确认', '你确认要<er>删除</er>这<er>'+oids.length+'</er>条信息吗?', function (r) {
            if (r) {  $.ajax({  type: 'POST', url: '/i/good/deleteByids.do', traditional:true,   data: { ids: oids },success: function (data) { if(data.status==200){ reloaddata();}else{ alert_errmsg( '删除失败!请稍后重试!'); } }}); }
        });
    } else {  alert_infomsg( '您还没有选择删除的信息哦'); }

};
//工具栏--》编辑
function tl_editData() {
    var selusers = getTableChecked();
    if (selusers.length > 0) {
        var data = selusers[0];
        editFromData(data);
    } else {
        alert_infomsg("请选择一个条数据进行操作");
    }
}
//工具栏--》添加
function tl_addDData(){
    initComp(true);
    $('#datadialog').dialog({title:'添加商品信息',iconCls:'main_ad',closed: false});
}
//======================================================================菜单事件function======================================================================================================================

//编辑信息
function editFromData(data){
    initComp(false);
    initimgs(data.id,3);
    if(data.img){ $("#img_logo").attr("src",sys.imgrooturl+data.img);}
    $('#from_data').form('load',data);
    $('#datadialog').dialog({title:'编辑商品信息',iconCls:'main_ad',closed: false});
}
//保存信息
function saveFromData(){
    if(imginfo.lock){ return;}
    var fromdata=$('#from_data');
    if (! fromdata.form('validate')) {  return;}else{  imginfo.lock=true; }
    var vo = {},formdata = new FormData(),parnArray =fromdata.serializeArray();
    $.each(parnArray, function (index, item) {
        formdata.append(item.name,item.value);
    });
    var logo=$("#file_logo")[0].files[0];
    if(logo){  formdata.append("logoimg",logo); }
    if(imginfo.addimg.length>0){
        $.each(imginfo.addimg, function (index, item) {
            formdata.append('file'+index, item);
        });
    }
    if(imginfo.delimgid.length>0){//删除图片的信息
        formdata.append('fileids', imginfo.delimgid);
        formdata.append('fileurls', imginfo.delimg);
    }
    $.ajax({
        url: "/i/good/add.do",
        data: formdata,
        traditional:true,
        processData: false,
        contentType: false,
        type: 'POST',
        error: function(request) {  imginfo.lock=false;alert_infomsg( '请刷新页面重试');},
        success: function (data) {
            imginfo.lock=false;
            $('#datadialog').dialog({closed: true})
            if(data.status!=200){ alert_errmsg(data.message);}
            reloaddata();
        }
    });
}

function print(){
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        var expfrom = $("<form>").attr('style', 'display:none').attr('method', 'post').attr('action', '/print.htm').attr('id', "expdataform");
        expfrom.attr("Content-Type", "application/json;charset=UTF-8").attr("target", "_blank");
        expfrom.append($("<input>").attr("name", "type").attr("value", 1));
        expfrom.append($("<input>").attr("name", "token").attr("value", tool.col_getsl()));
        expfrom.append($("<input>").attr("name", "ids").attr("value",oids[0]));
        expfrom.appendTo('body').submit().remove();
    }




}


//======================================================================格式化col=======================================================
function onDblClickRow(index, field) {editFromData(field);}//双击编辑数据详细信息

function init_table() {
//	var fottol  ={buttons: [{text:'添加用户',iconCls:'user_add',handler:addDromData},{text:'编辑用户',iconCls:'user_edit',handler:edituser},{text:'删除用户',iconCls:'user_del',handler:deleteUsers},"-"]};
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'name', title: '名称', width: 80, align: 'center', sortable: true},
        {field: 'compname', title: '企业名称', width: 80, align: 'center', sortable: true},
        {field: 'growname', title: '基地名称', width: 80, align: 'center', sortable: true},
        {field: 'goodtype', title: '品类', width: 80, align: 'center', sortable: true,formatter: col_goodtype,filter:false},
        {field: 'grade', title: '等级', width: 80, align: 'center', sortable: true,formatter: col_goodlave,filter:false},
        {field: 'price', title: '价格', width: 80, align: 'center', sortable: true,filter:false},
        {field: 'growth', title: '生长周期', width: 80, align: 'center', sortable: true,filter:false},
    ]];
    initTable("商品管理", "main_ad", "POST", "/i/good/list.do", queryParams, '#user_filter', null, col, true, onDblClickRow);
//    objTable.datagrid({singleSelect: true});//设置为单选
    crspsh();
}

//======================================================================初始化数据=======================================================
//初始化数据
$().ready(function () {
    init_table();
    $("#sel_goodtype").combobox("loadData", good_type);
    $("#sel_lave").combobox("loadData", good_lave);
    $("#sel_unit").combobox("loadData", good_unit);
});
//======================================================================初始化数据=======================================================


