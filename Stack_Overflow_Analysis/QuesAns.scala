//6. Number of questions with more than 2 answers
package com.df.stackoverflow

import scala.xml.XML

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import java.text.SimpleDateFormat
import java.lang.String
import scala.xml.Elem
import org.apache.spark.sql.SparkSession

object QuesAns {
	def main(args: Array[String]) = {
			System.setProperty("hadoop.home.dir", "D:\\ranjith\\setups\\hadoop_setups\\CDH5\\hadoop-2.5.0-cdh5.3.2")
			System.setProperty("spark.sql.warehouse.dir", "file:/D:/ranjith/setups/Spark_Setups/spark-2.0.0-bin-hadoop2.6/spark-warehouse")

			val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      val format2 = new SimpleDateFormat("yyyy-MM");

			val spark = SparkSession
				.builder
				.appName("AvgAnsTime")
				.master("local")
				.getOrCreate()
				
			//Read some example file to a test RDD
			val data = spark.read.textFile("E:\\test\\Posts1.xml").rdd


			val result = data.filter{line => {line.trim().startsWith("<row")}
			}
			.filter { line => {line.contains("PostTypeId=\"1\"")}
			}
			.map {line => {
			  val xml = XML.loadString(line)
			  (Integer.parseInt(xml.attribute("AnswerCount").getOrElse(0).toString()), line)
			  }
			}
			.filter{x => { x._1 > 2 }
			}
			.sortByKey(false)
			
			result.foreach { println }

			spark.stop
	}
}