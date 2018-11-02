var  goodTalbe=null,l_queryParams=null,
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
            objTable.datagrid({ url:'/i/trace/list.do', queryParams:r_queryParams });

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
v_record=['','护理','施肥','农药','环境','生长照片/视频','产品认证'], v_stage=["","幼苗期",'生长期','收获期'],
    kv_record=[ {id:  1	,text:"护理"}, {id: 2,text:"施肥"},{id: 3	,text:"农药"},{id: 4,text:"环境"}, {id: 5,text:"生长照片/视频"}, {id: 6,text:"产品认证"}],
    kv_stage=[{id:  1	,text:"幼苗期"}, {id: 2,text:"生长期"},{id: 3,text:"收获期"}];
function col_record(value,row,index){ return v_record[value]};
function col_stage(value,row,index){ return v_stage[value]};
function onDblClickRow(index, field) {editFromData(field);}//双击编辑数据详细信息


function init_right_comp() {
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'time', title: '时间', width: 80, align: 'center', sortable: true},
        {field: 'record', title: '事件', width: 80, align: 'center', sortable: true,formatter: col_record},
        {field: 'stage', title: '生长/生产阶段', width: 80, align: 'center', sortable: true,formatter: col_stage},
        {field: 'operator', title: '操作人', width: 80, align: 'center', sortable: true},
        {field: 'make', title: '备注', width: 80, align: 'center', sortable: true}
    ]];
    initTable("溯源管理", "main_ad", "POST", "", r_queryParams, '#tool_bar', null, col, true, onDblClickRow);
}
//======================================================================菜单事件function======================================================================================================================

function initComp(add){
    imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};
    $("#comp_imgList").empty();
    $("#img_logo").attr("src","");
    $('#from_data').form('clear');
    $("#sl_record").combobox("loadData", kv_record);
    $("#sl_stage").combobox("loadData", kv_stage);

}

//编辑信息
function editFromData(data){
    initComp(false);
    initimgs(data.id,4);
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
     // if(""==$("#hd_id").val()){ formdata.append("gid",$("#hd_gid").val());}
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
        url: "/i/trace/add.do",
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
    initComp(true);
    $("#hd_gid").attr("value",r_queryParams.gid);
    $('#datadialog').dialog({title:'添加商品信息',iconCls:'main_ad',closed: false});
}
//

