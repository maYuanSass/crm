<!DOCTYPE html>
<html>
<head>
    <title>字典管理</title>
    <#include "../common.ftl">
</head>
<body class="childrenBody">
<form class="layui-form">
    <blockquote class="layui-elem-quote quoteBox">
        <form class="layui-form">
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <input type="text" name="dataDicName" class="layui-input searchVal" placeholder="字典名称"/>
                </div>
                <a class="layui-btn search_btn" data-type="reload">
                    <i class="layui-icon">&#xe615;</i>
                    搜索
                </a>
            </div>
        </form>
    </blockquote>

    <table id="dataDicList" class="layui-table" lay-filter="dataDics"></table>

    <script type="text/html" id="toolbarDemo">
        <div class="layui-btn-container">
            <a class="layui-btn layui-btn-normal addNews_btn" lay-event="add">
                <i class="layui-icon">&#xe608;</i>
                添加
            </a>
            <a class="layui-btn layui-btn-danger delNews_btn" lay-event="del">
                <i class="layui-icon">&#xe640;</i>
                删除
            </a>
        </div>
    </script>

    <!--操作-->
    <script id="dataDicListBar" type="text/html">
        <a class="layui-btn layui-btn-xs" id="edit" lay-event="edit">编辑</a>
        <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
    </script>
</form>

<script type="text/javascript" src="${ctx}/js/dataDic/data_dic.js"></script>
</body>
</html>