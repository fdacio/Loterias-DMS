package br.com.daciosoftware.loteriasdms.processaarquivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyHtmlParse {

    private String fileHtmlParser;

    private MyHtmlParse(String fileHtml) { this.fileHtmlParser = fileHtml;}

    /**
     * @param fileHtml - path from html file e.g: /home/user/myfile.htm
     * @return a instance from MyHtmlParse
     */
    public static MyHtmlParse getInstance(String fileHtml) {
        return new MyHtmlParse(fileHtml);
    }

    /**
     * @param trow - Uma linha de uma tabela html
     * @return lista com todas as colunas dessa tabela
     */
    public static List<String> getTdsInTrow(String trow) {
        List<String> listTds = new ArrayList<>();
        String tagBegin = "<td";
        String tagEnd = "</td>";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < trow.length(); i++) {
            sb.append(trow.charAt(i));

            String textoTag = sb.toString();
            boolean containsBegin = textoTag.contains(tagBegin);
            boolean containsEnd = textoTag.contains(tagEnd);
            if (containsBegin && containsEnd) {
                int indiceStart = textoTag.indexOf(tagBegin);
                int indiceEnd = textoTag.indexOf(tagEnd);
                String td = textoTag.substring(indiceStart, indiceEnd + tagEnd.length());
                listTds.add(td);
                sb = new StringBuffer();
            }
        }
        return listTds;
    }

    /**
     * @param tag - Tag html ex: <td>TextoAqui</td>
     * @return Conteúdo da tag: TextoAqui
     */
    public static String getTextTag(String tag) {
        String tagBegin = ">";
        String tagEnd = "</";
        int indiceStart = tag.indexOf(tagBegin);
        int indiceEnd = tag.indexOf(tagEnd);
        return tag.substring(indiceStart + 1, indiceEnd).replace("&nbsp", "");
    }

    /**
     * @return a List from string with content html file
     * @throws IOException
     */
    private List<String> parseHtml() throws IOException {

        List<String> listHtml = new ArrayList<>();

        FileReader fileHtml = new FileReader(this.fileHtmlParser);

        BufferedReader reader = new BufferedReader(fileHtml);
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch != '\n') {
                    sb.append(ch);
                }
            }
            listHtml.add(sb.toString());
            sb = new StringBuffer();
        }
        reader.close();
        return listHtml;
    }

    /**
     * @return - Título do html
     * @throws IOException
     */

    public String getTitleHtml() throws IOException {
        String title = "";
        String tagBegin = "<title>";
        String tagEnd = "</title>";
        StringBuilder sb = new StringBuilder();
        List<String> html = this.parseHtml();
        for (String textoHtml : html) {
            for (int i = 0; i < textoHtml.length(); i++) {
                sb.append(textoHtml.charAt(i));
            }
            String textoTag = sb.toString();
            boolean containsBegin = textoTag.contains(tagBegin);
            boolean containsEnd = textoTag.contains(tagEnd);
            if (containsBegin && containsEnd) {
                int indiceStart = textoTag.indexOf(tagBegin);
                int indiceEnd = textoTag.indexOf(tagEnd);
                title = getTextTag(textoTag.substring(indiceStart, indiceEnd + tagEnd.length()));
                return title;
            }

        }
        return title;
    }

    /**
     * @return - Todas as linhas de uma tabela do html
     * @throws IOException
     */
    public List<String> getTrowsTable() throws IOException {
        List<String> listTableRow = new ArrayList<>();
        String tagBegin1 = "<tr";
        String tagBegin2 = "<tr>";
        String tagEnd = "</tr>";
        StringBuffer sb = new StringBuffer();
        List<String> html = this.parseHtml();
        for (String textoHtml : html) {
            for (int i = 0; i < textoHtml.length(); i++) {
                sb.append(textoHtml.charAt(i));
            }
            String textoTag = sb.toString();
            boolean containsBegin = textoTag.contains(tagBegin1) || textoTag.contains(tagBegin2);
            boolean containsEnd = textoTag.contains(tagEnd);
            if (containsBegin && containsEnd) {
                int indiceStart = textoTag.indexOf(tagBegin1);
                int indiceEnd = textoTag.indexOf(tagEnd);
                String trow = textoTag.substring(indiceStart, indiceEnd + tagEnd.length());
                listTableRow.add(trow);
                sb = new StringBuffer();
            }
        }

        return listTableRow;
    }

}
