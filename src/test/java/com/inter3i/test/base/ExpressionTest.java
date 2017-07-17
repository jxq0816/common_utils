/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/06/07
 * Description:
 *
 */

package com.inter3i.test.base;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.expression.ExpressionEngine;
import com.inter3i.base.expression.entity.ExpressionEntity;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTest {
    public static void main(String[] args) {
        int x = 1, y = 1;
//        Object z = add.calculate(x, y);
//        System.out.println("result is :" + z);
        Character a = Character.valueOf('中');
        Character b = Character.valueOf('中');
        System.out.println(a == b ? "等于" : "不等于");
        System.out.println(a.equals(b) ? "等于" : "不等于");
        Long long1 = Long.valueOf(8);
        Long long2 = Long.valueOf(8);
        System.out.println(long1 == long2 ? "等于" : "不等于");

        // student.age ++ > 18 ? "chengnian":"weichengnian"

        // ==
        // 常量: "chengnian"

        //and
        //sex == male

        ExpressionEntity expressionEntity = new ExpressionEntity();//总表达式
        expressionEntity.setType(ExpressionEntity.OPERATOR_TYPE_LOGIC);
        expressionEntity.setOperator("and");
        List<ExpressionEntity> childExpression = new ArrayList<>(2);//两个子表达式
        expressionEntity.setChildExpressions(childExpression);

        ExpressionEntity firstExpressionEntity = new ExpressionEntity();//第一个子表达式实体
        childExpression.add(firstExpressionEntity);

        firstExpressionEntity.setOperator("==");
        firstExpressionEntity.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        List<ExpressionEntity> childsOfFirst = new ArrayList<>(2);//第一个子表达
        firstExpressionEntity.setChildExpressions(childsOfFirst);

        ExpressionEntity sanMu = new ExpressionEntity();//三目
        childsOfFirst.add(sanMu);//三目加入第一个子表达式
        sanMu.setType(ExpressionEntity.OPERATOR_TYPE_TERNARY);
        sanMu.setOperator("?");

        List<ExpressionEntity> childsOfSanmu = new ArrayList<>(3);//三目子表达式
        sanMu.setChildExpressions(childsOfSanmu);


        //student.age ++
        ExpressionEntity danmuExpress = new ExpressionEntity();//单目表达式
        danmuExpress.setOperator("++");
        danmuExpress.setType(ExpressionEntity.OPERATOR_TYPE_UNARY);
        ExpressionEntity.ValueTermEntity valueTermEntity = new ExpressionEntity.ValueTermEntity();
        valueTermEntity.setDataType("int");
        valueTermEntity.setValueType("pathExpress");
        valueTermEntity.setValue("student.age");
        valueTermEntity.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);

        List<ExpressionEntity> childsOfDanmu = new ArrayList<>(1);//单目子表达式
        childsOfDanmu.add(valueTermEntity);
        danmuExpress.setChildExpressions(childsOfDanmu);

        // x > 18
        ExpressionEntity moreThan18Ofsanmu = new ExpressionEntity();
        moreThan18Ofsanmu.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        moreThan18Ofsanmu.setOperator(">");

        List<ExpressionEntity> childsOfLogic = new ArrayList<>(2);//满18岁的 子逻辑列表
        childsOfLogic.add(danmuExpress);//单子逻辑列表添加 单目表达式
        moreThan18Ofsanmu.setChildExpressions(childsOfLogic);
        childsOfSanmu.add(moreThan18Ofsanmu);//三目表达式添加满18

        // 18 constants
        ExpressionEntity.ValueTermEntity isAdult = new ExpressionEntity.ValueTermEntity();
        isAdult.setDataType("int");
        isAdult.setValueType("constants");
        isAdult.setValue("18");
        isAdult.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfLogic.add(isAdult);


        //false value of danmu
        ExpressionEntity.ValueTermEntity falseValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        falseValueOfDanMu.setDataType("String");
        falseValueOfDanMu.setValueType("constants");
        falseValueOfDanMu.setValue("weichengnian");
        falseValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(falseValueOfDanMu);//三目表达式添加false value of danmu

        //true value of danmu
        ExpressionEntity.ValueTermEntity trueValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        trueValueOfDanMu.setDataType("String");
        trueValueOfDanMu.setValueType("constants");
        trueValueOfDanMu.setValue("chengnian");
        trueValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(trueValueOfDanMu);//三目表达式添加true value of danmu

        // == "chengnian"
        ExpressionEntity.ValueTermEntity chengnianBiaoZhun = new ExpressionEntity.ValueTermEntity();
        chengnianBiaoZhun.setDataType("String");
        chengnianBiaoZhun.setValueType("constants");
        chengnianBiaoZhun.setValue("chengnian");
        chengnianBiaoZhun.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfFirst.add(chengnianBiaoZhun);//成年加入第一个子表达式


        // **************** sex == male ***************/
        //sex == male
        ExpressionEntity second = new ExpressionEntity();//第二个子表达式实体
        second.setOperator("==");
        second.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);


        List<ExpressionEntity> childsOfSecond = new ArrayList<>(2);//第二个子表达式
        second.setChildExpressions(childsOfSecond);
        childExpression.add(second);

        //sex
        ExpressionEntity.ValueTermEntity studentSex = new ExpressionEntity.ValueTermEntity();
        studentSex.setDataType("String");
        studentSex.setValueType("pathExpress");
        studentSex.setValue("student.sex");
        studentSex.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSecond.add(studentSex);//第二个子表达式添加学生性别

        ExpressionEntity.ValueTermEntity constantsMale = new ExpressionEntity.ValueTermEntity();
        constantsMale.setDataType("String");
        constantsMale.setValueType("constants");
        constantsMale.setValue("male");
        constantsMale.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSecond.add(constantsMale);//第二个子表达式添加？？？
        // **************** sex == male ***************/

        ExpressionEngine.Expression expression = ExpressionEntity.conver2Expression(expressionEntity);//总表达式
        JSONObject content = new JSONObject();

        JSONObject student = new JSONObject();
        content.put("student", student);
        student.put("name", "zhangsan");
        student.put("sex", "male");
        student.put("age", 1);
        Object result = expression.evel(content);
        System.out.println("result is :[" + result + "].");
    }
    //(int x,int y) -> x+y;
}
