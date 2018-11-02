var  goodTalbe=null,l_queryParams=null,l_lock=false,
r_queryParams = {page: null, rows: null,  keyword: null}


//1.左侧=======================================================================================================================
var  good_type=[ {id:  1	,text:"水果"}, {id: 2,text:"	蔬菜"},{id: 3	,text:"坚果"},{id: 4,text:"畜禽"}, {id: 5,text:"食品"}],
    good_lave=[{id:  1	,text:"精品"}, {id: 2,text:"特优"},{id: 3	,text:"一级"},{id: 4,text:"二级"}, {id: 5,text:"精选"}];

function init_left_comp(){
    //初始化过滤条件
    $("#sel_goodtype").combobox("loadData", good_type);
    $("#sel_lave").combobox("loadData", good_lave);
    $('#sl_compid').combobox({ url:'/i/company/allList.do', valueField:'id', textField:'text', onSelect: function(rec){
            var url = '/i/grow/allList.do?cid='+rec.id;
            $('#sl_growid').combobox('reload', url);
    }});
    //初始化商品表格
    goodTalbe=$('#goodTable').datagrid({
        url:'/i/good/list.do', method:"POST", queryParams:l_queryParams,fit:true,
        fitColumns:true, remoteSort: false, striped:true,pagination:true,singleSelect: true,
        pageSize:20,pageList:[20,10,50,100,200,500],
        columns:[[ {field: 'id', title: 'ID', sortable: true}, {field: 'name', title: '名称', width: 80, align: 'center', sortable: true}, {field: 'compname', title: '企业名称', width: 80, align: 'center', sortable: true}, {field: 'growname', title: '基地名称', width: 80, align: 'center', sortable: true}  ]]
        ,  onLoadError:clearTable,onDblClickRow:function( index, field){
            r_queryParams.gid=field.id;
            r_queryParams.token=tool.col_getsl();
            objTable.datagrid({ url:'/i/auth/list.do', queryParams:r_queryParams });
        }
     });
}
function filter_ffrom(){
    l_queryParams = {page: null, rows: null,  keyword: null};
   var  filter=  $("#filter_from").serializeArray();
    $.each(filter, function (index, item) {
        if(item.value!=""){
            l_queryParams[item.name]=item.value;
        }
    });
   goodTalbe.datagrid('reload',l_queryParams);
}
function cler_ffrom() {
    l_queryParams = {page: null, rows: null,  keyword: null};
    goodTalbe.datagrid('reload',l_queryParams);
    $("#filter_from").form('clear');
}

//======================================================================右边面板=======================================================
function onDblClickRow(index, field) { }//双击编辑数据详细信息

function init_right_comp() {
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'uuid', title: 'UUID', width: 80, align: 'center', sortable: true},
        {field: 'status', title: '是否有效', width: 80, align: 'status', sortable: true,formatter: tool.col_isdeal},
        {field: 'Invalidtime', title: '失效时间', width: 80, align: 'center', sortable: true},
        {field: 'count', title: '查询次数', width: 80, align: 'center', sortable: true},
        {field: 'addtime', title: '出库时间', width: 80, align: 'center', sortable: true}
    ]];
    initTable("真伪管理", "icon-authe", "POST", "", r_queryParams, '#tool_bar', null, col, true, onDblClickRow);
}
//======================================================================菜单事件function======================================================================================================================

//打印  ,int  token,int type,int []ids
function print(){
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        var expfrom = $("<form>").attr('style', 'display:none').attr('method', 'post').attr('action', '/print.htm').attr('id', "expdataform");
        expfrom.attr("Content-Type", "application/json;charset=UTF-8").attr("target", "_blank");
        expfrom.append($("<input>").attr("name", "type").attr("value", 2));
        expfrom.append($("<input>").attr("name", "token").attr("value", tool.col_getsl()));
        expfrom.append($("<input>").attr("name", "ids").attr("value",oids));
        expfrom.appendTo('body').submit().remove();
    }
}


//保存信息
function saveFromData(){
    if(l_lock){ return;}
    var fromdata=$('#from_data');
    if (! fromdata.form('validate')) {  return;}else{  l_lock=true; }
    l_lock=false;

        var expfrom = $("<form>").attr('style', 'display:none').attr('method', 'post').attr('action', '/i/auth/adddAndPrint.htm').attr('id', "expdataform");
        expfrom.attr("Content-Type", "application/json;charset=UTF-8").attr("target", "_blank");
        expfrom.append($("<input>").attr("name", "size").attr("value", $("#no_size").val()));
        expfrom.append($("<input>").attr("name", "token").attr("value", tool.col_getsl()));
        expfrom.append($("<input>").attr("name", "gid").attr("value", r_queryParams.gid));
        expfrom.appendTo('body').submit().remove();
    $('#datadialog').dialog({closed: true})
    // $.ajax({
    //     url: "/i/auth/add.do",
    //     data: {"size":$("#no_size").val(),token:tool.col_getsl(),gid:r_queryParams.gid},
    //     type: 'POST',
    //     error: function(request) { l_lock=false;alert_infomsg( '请刷新页面重试');},
    //     success: function (data) {
    //         l_lock=false;
    //         $('#datadialog').dialog({closed: true})
    //         if(data.status!=200){ alert_errmsg(data.message);}
    //         reloaddata();
    //     }
    // });
}







//======================================================================初始化数据=======================================================
//初始化数据
$().ready(function () {
    init_left_comp();
    init_right_comp();
});
//======================================================================初始化数据=======================================================
//工具栏--》删除
var tl_deleteData = function () {
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        $.messager.confirm('删除确认', '你确认要<er>删除</er>这<er>'+oids.length+'</er>条信息吗?', function (r) {
            if (r) {  $.ajax({  type: 'POST', url: '/i/trace/deleteByids.do', traditional:true,   data: { ids: oids },success: function (data) { if(data.status==200){ reloaddata();}else{ alert_errmsg( '删除失败!请稍后重试!'); } }}); }
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
    if(r_queryParams.gid==undefined){ alert_errmsg("请选择一个商品后操作！");   return;}
    $('#datadialog').dialog({closed: false});
}
//

