package de.hska.iwi.graphtraversal.output;

import de.hska.iwi.graphtraversal.graph.State;
import de.hska.iwi.graphtraversal.input.Strategy;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Exports the result as a step-by-step HTML table.
 * <p>
 * Using the Pen "Data Table" by Andrew Lassetter (@alassetter)
 * Source: https://codepen.io/alassetter/pen/cyrfB
 * License: MIT License (https://blog.codepen.io/legal/licensing)
 */
public class TableWriter extends Exporter {

    private static final String HEAD_PRE_TITLE = "<!DOCTYPE html><html lang=\"en\"><head> <meta charset=\"UTF-8\"> <title>";
    private static final String HEAD_POST_TITLE = " </title> <meta name=\"viewport\" content=\"initial-scale=1.0; maximum-scale=1.0; width=device-width;\"> <style media=\"screen\" type=\"text/css\"> @import url(https://fonts.googleapis.com/css?family=Roboto:400,500,700,300,100); body{background-color: #3e94ec; font-family: \"Roboto\", helvetica, arial, sans-serif; font-size: 16px; font-weight: 400; text-rendering: optimizeLegibility;}div.table-title{display: block; margin: auto; max-width: 600px; padding:5px; width: 100%;}.table-title h3{color: #fafafa; font-size: 30px; font-weight: 400; font-style:normal; font-family: \"Roboto\", helvetica, arial, sans-serif; text-shadow: -1px -1px 1px rgba(0, 0, 0, 0.1); text-transform:uppercase;}/*** Table Styles **/ .table-fill{background: white; border-radius:3px; border-collapse: collapse; height: 320px; margin: auto; max-width: 600px; padding:5px; width: 100%; box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1); animation: float 5s infinite;}th{color:#D5DDE5;; background:#1b1e24; border-bottom:4px solid #9ea7af; border-right: 1px solid #343a45; font-size:23px; font-weight: 100; padding:24px; text-align:left; text-shadow: 0 1px 1px rgba(0, 0, 0, 0.1); vertical-align:middle;}th:first-child{border-top-left-radius:3px;}th:last-child{border-top-right-radius:3px; border-right:none;}tr{border-top: 1px solid #C1C3D1; border-bottom-: 1px solid #C1C3D1; color:#666B85; font-size:16px; font-weight:normal; text-shadow: 0 1px 1px rgba(256, 256, 256, 0.1);}tr:hover td{background:#4E5066; color:#FFFFFF; border-top: 1px solid #22262e;}tr:first-child{border-top:none;}tr:last-child{border-bottom:none;}tr:nth-child(odd) td{background:#EBEBEB;}tr:nth-child(odd):hover td{background:#4E5066;}tr:last-child td:first-child{border-bottom-left-radius:3px;}tr:last-child td:last-child{border-bottom-right-radius:3px;}td{background:#FFFFFF; padding:20px; text-align:left; vertical-align:middle; font-weight:300; font-size:18px; text-shadow: -1px -1px 1px rgba(0, 0, 0, 0.1); border-right: 1px solid #C1C3D1;}td:last-child{border-right: 0px;}th.text-left{text-align: left;}th.text-center{text-align: center;}th.text-right{text-align: right;}td.text-left{text-align: left;}td.text-center{text-align: center;}td.text-right{text-align: right;}</style></head>";
    private static final String BODY_PRE_HEADLINE = " <body> <div class=\"table-title\"> <h3>";
    private static final String BODY_PRE_TABLE_HEAD = " </h3> </div><table class=\"table-fill\"> <thead> <tr>";
    private static final String BODY_PRE_TABLE_ROWS = " </tr></thead> <tbody class=\"table-hover\">";
    private static final String BODY_POST = " </tbody> </table></body></html>";

    private final String htmlTitle;
    private final String headline;
    private final Strategy strategy;

    public TableWriter(String filename, Strategy strategy) {
        super(filename);
        this.strategy = strategy;
        this.htmlTitle = strategy.getName();
        this.headline = strategy.getName();
    }

    private String generateTableHeader() {

        StringBuilder tableHeader = new StringBuilder();

        tableHeader.append("<th class=\"text-right\">" + "#" + "</th>");
        tableHeader.append("<th class=\"text-center\">" + "Q" + "</th>");
        tableHeader.append("<th class=\"text-left\">" + "v" + "</th>");
        tableHeader.append("<th class=\"text-left\">" + "v'" + "</th>");
        tableHeader.append("<th class=\"text-center\">" + "N" + "</th>");

        if (strategy == Strategy.DFS) {
            tableHeader.append("<th class=\"text-center\">" + "u" + "</th>");
        }

        tableHeader.append("<th class=\"text-center\">" + "d" + "</th>");

        if (strategy == Strategy.DFS) {
            tableHeader.append("<th class=\"text-center\">" + "f" + "</th>");
        }

        return tableHeader.toString();
    }

    private String generateTableRow(State state) {

        // TODO: State attributes polishing

        StringBuilder tableRow = new StringBuilder();

        tableRow.append("<tr><td class=\"text-left\">" + state.getIteration());
        tableRow.append("</td><td class=\"text-left\">" + Arrays.toString(state.getCollection()));
        tableRow.append("</td><td class=\"text-left\">" + state.getCurrent());
        tableRow.append("</td><td class=\"text-left\">" + state.getNext());

        tableRow.append("</td><td class=\"text-left\">");
        for (int i = 0; i < state.getNeighbors().length; i++) {
            tableRow.append(Arrays.toString(state.getNeighbors()[i]));
            if (i < state.getNeighbors().length - 1) {
                tableRow.append("<br/>");
            }
        }

        tableRow.append("</td><td class=\"text-left\">");

        switch (strategy) {
            case BFS:
                for (int i = 0; i < state.getDistance().length; i++) {
                    tableRow.append(state.getDistance()[i]);
                    if (i < state.getDistance().length - 1) {
                        tableRow.append("<br/>");
                    }
                }
                break;

            case DFS:
                tableRow.append(state.getTimer());
                tableRow.append("</td><td class=\"text-left\">");

                for (int i = 0; i < state.getArrivals().length; i++) {
                    tableRow.append(state.getArrivals()[i]);
                    if (i < state.getArrivals().length - 1) {
                        tableRow.append("<br/>");
                    }
                }
                tableRow.append("</td><td class=\"text-left\">");

                for (int i = 0; i < state.getDepartures().length; i++) {
                    tableRow.append(state.getDepartures()[i]);
                    if (i < state.getDepartures().length - 1) {
                        tableRow.append("<br/>");
                    }
                }
                break;
        }

        tableRow.append("</td></tr>");
        return tableRow.toString();
    }

    @Override
    public void exportResults(List<State> log) {

        StringBuilder table = new StringBuilder();

        table.append(HEAD_PRE_TITLE);
        table.append(htmlTitle);
        table.append(HEAD_POST_TITLE);
        table.append(BODY_PRE_HEADLINE);
        table.append(headline);
        table.append(BODY_PRE_TABLE_HEAD);
        table.append(generateTableHeader());
        table.append(BODY_PRE_TABLE_ROWS);

        for (State state : log) {
            table.append(generateTableRow(state));
        }
        table.append(BODY_POST);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename + ".html"), "utf-8"))) {
            writer.write(table.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
