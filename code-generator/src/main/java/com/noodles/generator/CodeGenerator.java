package com.noodles.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Noodles
 * @since 2022-09-28
 */
public class CodeGenerator {
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/crawler?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    private final static String MODULE_NAME = "crawler-service";
    private final static String AUTHOR = "Noodles";
    private final static String PARENT_PACKAGE = "com.noodles.crawler";
    private final static String[] TABLE_NAME = new String[]{"sys_request_info"};
    private final static String[] TABLE_PREFIX = new String[]{"sys_"};

    public static void main(String[] args) {
        //entity表字段属性List，用于生成时间自动填充属性
        List<IFill> iFills = new ArrayList<>();
        iFills.add(new Column("create_time", FieldFill.INSERT));
        iFills.add(new Column("update_time", FieldFill.INSERT_UPDATE));

        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    String s = System.getProperty("user.dir") + "\\" + MODULE_NAME + "\\src\\main\\java";
                    builder
                            .author(AUTHOR)
                            .fileOverride()
                            .enableSwagger()
                            .outputDir(s)
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_PACKAGE)
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, MODULE_NAME + "\\src\\main\\resources\\mapper"));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude(TABLE_NAME)
                            // 设置过滤表前缀
                            .addTablePrefix(TABLE_PREFIX)
                            .entityBuilder()
                            .idType(IdType.AUTO)
                            .enableLombok()
                            //生成时间自动填充属性
                            .addTableFills(iFills)
                            //开启@RestController风格
                            .controllerBuilder().enableRestStyle()
                            //去掉默认的I前缀
                            .serviceBuilder().formatServiceFileName("%sService");
                })
                //使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
