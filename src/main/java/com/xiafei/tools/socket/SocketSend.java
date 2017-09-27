package com.xiafei.tools.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * <P>Description:  . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/18</P>
 * <P>UPDATE DATE: 2017/8/18</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class SocketSend {
    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.4.11", 20002);) {

            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.print("请输入: \t");
            String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "\n" +
                    "<service>\n" +
                    "  <sys-header>\n" +
                    "    <data name=\"SYS_HEAD\">\n" +
                    "      <struct>\n" +
                    "        <data name=\"APPR_FLAG\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"APPR_USER_ID\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"AUTH_FLAG\">\n" +
                    "          <field length=\"1\" scale=\"0\" type=\"string\">M</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"BRANCH_ID\">\n" +
                    "          <field length=\"6\" scale=\"0\" type=\"string\">800001</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"CONSUMER_ID\">\n" +
                    "          <field length=\"3\" scale=\"0\" type=\"string\">MBS</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"CUSTOMER_ID\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"MESSAGE_CODE\">\n" +
                    "          <field length=\"4\" scale=\"0\" type=\"string\">7501</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"MESSAGE_TYPE\">\n" +
                    "          <field length=\"4\" scale=\"0\" type=\"string\">1200</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"MODULE_ID\">\n" +
                    "          <field length=\"2\" scale=\"0\" type=\"string\">DP</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"PROGRAM_ID\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"REVERSAL_TRAN_TYPE\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"SEQ_NO\">\n" +
                    "          <field length=\"15\" scale=\"0\" type=\"string\">NL0000342369603</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"SERVER_ID\">\n" +
                    "          <field length=\"13\" scale=\"0\" type=\"string\">10.75.170.236</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"SERVICE_CODE\">\n" +
                    "          <field length=\"21\" scale=\"0\" type=\"string\">SVR_PTS2_NONFINANCIAL</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"SOURCE_BRANCH_NO\">\n" +
                    "          <field length=\"13\" scale=\"0\" type=\"string\">ETC.trans.zpk</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"SOURCE_TYPE\">\n" +
                    "          <field length=\"2\" scale=\"0\" type=\"string\">NL</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"TRAN_CODE\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"TRAN_DATE\">\n" +
                    "          <field length=\"8\" scale=\"0\" type=\"string\">20170816</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"TRAN_MODE\">\n" +
                    "          <field length=\"6\" scale=\"0\" type=\"string\">ONLINE</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"TRAN_TIMESTAMP\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"TRAN_TYPE\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"USER_ID\">\n" +
                    "          <field length=\"5\" scale=\"0\" type=\"string\">NLS02</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"USER_LANG\">\n" +
                    "          <field length=\"7\" scale=\"0\" type=\"string\">CHINESE</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"WS_ID\">\n" +
                    "          <field length=\"2\" scale=\"0\" type=\"string\">05</field>\n" +
                    "        </data>\n" +
                    "      </struct>\n" +
                    "    </data>\n" +
                    "  </sys-header>\n" +
                    "  <app-header>\n" +
                    "    <data name=\"APP_HEAD\">\n" +
                    "      <struct>\n" +
                    "        <data name=\"CURRENT_NUM\">\n" +
                    "          <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"FILE_PATH\">\n" +
                    "          <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "        </data>\n" +
                    "        <data name=\"PAGE_END\">\n" +
                    "          <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"PAGE_START\">\n" +
                    "          <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"PGUP_OR_PGDN\">\n" +
                    "          <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "        </data>\n" +
                    "        <data name=\"TOTAL_NUM\">\n" +
                    "          <field length=\"2\" scale=\"0\" type=\"string\">-1</field>\n" +
                    "        </data>\n" +
                    "      </struct>\n" +
                    "    </data>\n" +
                    "  </app-header>\n" +
                    "  <body>\n" +
                    "    <data name=\"AGENT_FLAG\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"BRANCH\">\n" +
                    "      <field length=\"6\" scale=\"0\" type=\"string\">800001</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"BRANCH_FLAG\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">C</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"BUSI_TYPE\">\n" +
                    "      <field length=\"4\" scale=\"0\" type=\"string\">A100</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"CCY\">\n" +
                    "      <field length=\"3\" scale=\"0\" type=\"string\">CNY</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"DIRECTION\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"DRAW_TYPE\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">S</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"FAST_FLAG\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">0</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"MSG_SPARE6\">\n" +
                    "      <field length=\"5\" scale=\"0\" type=\"string\">|NORM</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"OPTIONS\">\n" +
                    "      <field length=\"2\" scale=\"0\" type=\"string\">01</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYEE_ACCT\">\n" +
                    "      <field length=\"8\" scale=\"0\" type=\"string\">qixiafei</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYEE_BANK_CODE\">\n" +
                    "      <field length=\"19\" scale=\"0\" type=\"string\">6235730100000117870</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYEE_NAME\">\n" +
                    "      <field length=\"3\" scale=\"0\" type=\"string\">榻愰湠椋?/field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYEE_RCV_BANK\">\n" +
                    "      <field length=\"19\" scale=\"0\" type=\"string\">6235730100000117870</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYER_ACCT\">\n" +
                    "      <field length=\"11\" scale=\"0\" type=\"string\">niuxiaolong</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"PAYER_NAME\">\n" +
                    "      <field length=\"3\" scale=\"0\" type=\"string\">鐗涙檽榫?/field>\n" +
                    "    </data>\n" +
                    "    <data name=\"POSTSCRIPT\">\n" +
                    "      <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "    </data>\n" +
                    "    <data name=\"PRIORITY\">\n" +
                    "      <field length=\"4\" scale=\"0\" type=\"string\">NORM</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"REMARK\">\n" +
                    "      <field length=\"0\" scale=\"0\" type=\"string\"/>\n" +
                    "    </data>\n" +
                    "    <data name=\"SOURCE_MODULE\">\n" +
                    "      <field length=\"6\" scale=\"0\" type=\"string\">800001</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"TRAN_AMT\">\n" +
                    "      <field length=\"5\" scale=\"1\" type=\"double\">100.0</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"TRAN_CASH_FLAG\">\n" +
                    "      <field length=\"1\" scale=\"0\" type=\"string\">1</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"TRAN_CATEGORY\">\n" +
                    "      <field length=\"5\" scale=\"0\" type=\"string\">02102</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"TRAN_CLASS\">\n" +
                    "      <field length=\"3\" scale=\"0\" type=\"string\">CTC</field>\n" +
                    "    </data>\n" +
                    "    <data name=\"TRUSTED_PAY_NO\">\n" +
                    "      <field length=\"6\" scale=\"0\" type=\"string\">123456</field>\n" +
                    "    </data>\n" +
                    "  </body>\n" +
                    "</service>\n";
            out.writeUTF(str);
            socket.shutdownOutput();
            String ret = input.readUTF();
            System.out.println("服务器端返回过来的是: " + ret);
            // 如接收到 "OK" 则断开连接
            socket.shutdownInput();
            out.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
