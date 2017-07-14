package com.xiafei.tools.generatesource;

import java.io.Serializable;
import java.util.Date;

/**
 * <P>Description: ProductFeepayPO. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/07/14</P>
 * <P>UPDATE DATE: 2017/07/14</P>
 *
 * @author 齐霞飞
 * @version 1.0
 * @since JDK 1.7.0
 */
@SuppressWarnings("unused")
public class ProductFeepayPO implements Serializable {

    /**
     * .
     */
    private Long id;

    /**
     * 产品名称.
     */
    private String productName;

    /**
     * 产品编码.
     */
    private String productCode;

    /**
     * 产品总金额.
     */
    private Long productTotalAmount;

    /**
     * 实际募集金额.
     */
    private Long productRecruitAmount;

    /**
     * 是否交易所发行：0-否，1-是.
     */
    private Byte isTransactionPublish;

    /**
     * 交易所费用结算比例.
     */
    private String tradingFeeRate;

    /**
     * 计算基数1-360/2-365.
     */
    private Byte bearingBase;

    /**
     * 采购成本.
     */
    private String procurementCost;

    /**
     * 产品加息总金额.
     */
    private String totalInterestAmount;

    /**
     * 期限(天).
     */
    private Integer investmentHorizon;

    /**
     * 预期年化收益率.
     */
    private String userAnnualRate;

    /**
     * 交易所费用计算方式(1:年化收取 2:分次收取).
     */
    private Byte tradingFeeCalcType;

    /**
     * 交易所费用收取方式(1:不坐扣 2:坐扣).
     */
    private Byte tradingFeeChargeType;

    /**
     * 结算财顾费.
     */
    private Long clearingAdviceFee;

    /**
     * 结算服务费.
     */
    private Long clearingServiceFee;

    /**
     * 结算交易所通道费.
     */
    private Long clearingExchangeFee;

    /**
     * 是否费用计算完成：0-否，1-是 ,9-数据错误.
     */
    private Byte feeCalDone;

    /**
     * 是否内部Spv：0-否，1-是.
     */
    private Byte isSpv;

    /**
     * 项目数.
     */
    private Integer projectNum;

    /**
     * 产品状态:0-草稿；1-待审核；2-审核通过；3-审核不通过；4-募集完成.
     */
    private Byte productState;

    /**
     * 转账状态:0-未申请；1-已申请；.
     */
    private Byte transferAccountsStatus;

    /**
     * 费用确认状态:0-未确认；1-已确认；.
     */
    private Byte costConfirmStatus;

    /**
     * 财顾费支付状态:0-未申请；1-已申请；.
     */
    private Byte payCgStatus;

    /**
     * 交易所通道费支付状态:0-未申请；1-已申请；.
     */
    private Byte payJysStatus;

    /**
     * 乐信平台支付状态:0-未申请；1-已申请；.
     */
    private Byte payFwStatus;

    /**
     * 募集开始日.
     */
    private Date recruitStartTime;

    /**
     * 募集结束日.
     */
    private Date recruitEndTime;

    /**
     * 商户编码.
     */
    private String merchantCode;

    /**
     * 商户名称.
     */
    private String merchantName;

    /**
     * 商户开户行名称.
     */
    private String merchantBank;

    /**
     * 商户银行账户号.
     */
    private String merchantBankNo;

    /**
     * 供应商编码.
     */
    private String venderCode;

    /**
     * 供应商名称.
     */
    private String venderName;

    /**
     * 供应商开户行名称.
     */
    private String venderBank;

    /**
     * 供应商银行账户号.
     */
    private String venderBankNo;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 创建时间.
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(final Long pId) {
        this.id = pId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String pProductName) {
        this.productName = pProductName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(final String pProductCode) {
        this.productCode = pProductCode;
    }

    public Long getProductTotalAmount() {
        return productTotalAmount;
    }

    public void setProductTotalAmount(final Long pProductTotalAmount) {
        this.productTotalAmount = pProductTotalAmount;
    }

    public Long getProductRecruitAmount() {
        return productRecruitAmount;
    }

    public void setProductRecruitAmount(final Long pProductRecruitAmount) {
        this.productRecruitAmount = pProductRecruitAmount;
    }

    public Byte getIsTransactionPublish() {
        return isTransactionPublish;
    }

    public void setIsTransactionPublish(final Byte pIsTransactionPublish) {
        this.isTransactionPublish = pIsTransactionPublish;
    }

    public String getTradingFeeRate() {
        return tradingFeeRate;
    }

    public void setTradingFeeRate(final String pTradingFeeRate) {
        this.tradingFeeRate = pTradingFeeRate;
    }

    public Byte getBearingBase() {
        return bearingBase;
    }

    public void setBearingBase(final Byte pBearingBase) {
        this.bearingBase = pBearingBase;
    }

    public String getProcurementCost() {
        return procurementCost;
    }

    public void setProcurementCost(final String pProcurementCost) {
        this.procurementCost = pProcurementCost;
    }

    public String getTotalInterestAmount() {
        return totalInterestAmount;
    }

    public void setTotalInterestAmount(final String pTotalInterestAmount) {
        this.totalInterestAmount = pTotalInterestAmount;
    }

    public Integer getInvestmentHorizon() {
        return investmentHorizon;
    }

    public void setInvestmentHorizon(final Integer pInvestmentHorizon) {
        this.investmentHorizon = pInvestmentHorizon;
    }

    public String getUserAnnualRate() {
        return userAnnualRate;
    }

    public void setUserAnnualRate(final String pUserAnnualRate) {
        this.userAnnualRate = pUserAnnualRate;
    }

    public Byte getTradingFeeCalcType() {
        return tradingFeeCalcType;
    }

    public void setTradingFeeCalcType(final Byte pTradingFeeCalcType) {
        this.tradingFeeCalcType = pTradingFeeCalcType;
    }

    public Byte getTradingFeeChargeType() {
        return tradingFeeChargeType;
    }

    public void setTradingFeeChargeType(final Byte pTradingFeeChargeType) {
        this.tradingFeeChargeType = pTradingFeeChargeType;
    }

    public Long getClearingAdviceFee() {
        return clearingAdviceFee;
    }

    public void setClearingAdviceFee(final Long pClearingAdviceFee) {
        this.clearingAdviceFee = pClearingAdviceFee;
    }

    public Long getClearingServiceFee() {
        return clearingServiceFee;
    }

    public void setClearingServiceFee(final Long pClearingServiceFee) {
        this.clearingServiceFee = pClearingServiceFee;
    }

    public Long getClearingExchangeFee() {
        return clearingExchangeFee;
    }

    public void setClearingExchangeFee(final Long pClearingExchangeFee) {
        this.clearingExchangeFee = pClearingExchangeFee;
    }

    public Byte getFeeCalDone() {
        return feeCalDone;
    }

    public void setFeeCalDone(final Byte pFeeCalDone) {
        this.feeCalDone = pFeeCalDone;
    }

    public Byte getIsSpv() {
        return isSpv;
    }

    public void setIsSpv(final Byte pIsSpv) {
        this.isSpv = pIsSpv;
    }

    public Integer getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(final Integer pProjectNum) {
        this.projectNum = pProjectNum;
    }

    public Byte getProductState() {
        return productState;
    }

    public void setProductState(final Byte pProductState) {
        this.productState = pProductState;
    }

    public Byte getTransferAccountsStatus() {
        return transferAccountsStatus;
    }

    public void setTransferAccountsStatus(final Byte pTransferAccountsStatus) {
        this.transferAccountsStatus = pTransferAccountsStatus;
    }

    public Byte getCostConfirmStatus() {
        return costConfirmStatus;
    }

    public void setCostConfirmStatus(final Byte pCostConfirmStatus) {
        this.costConfirmStatus = pCostConfirmStatus;
    }

    public Byte getPayCgStatus() {
        return payCgStatus;
    }

    public void setPayCgStatus(final Byte pPayCgStatus) {
        this.payCgStatus = pPayCgStatus;
    }

    public Byte getPayJysStatus() {
        return payJysStatus;
    }

    public void setPayJysStatus(final Byte pPayJysStatus) {
        this.payJysStatus = pPayJysStatus;
    }

    public Byte getPayFwStatus() {
        return payFwStatus;
    }

    public void setPayFwStatus(final Byte pPayFwStatus) {
        this.payFwStatus = pPayFwStatus;
    }

    public Date getRecruitStartTime() {
        return recruitStartTime;
    }

    public void setRecruitStartTime(final Date pRecruitStartTime) {
        this.recruitStartTime = pRecruitStartTime;
    }

    public Date getRecruitEndTime() {
        return recruitEndTime;
    }

    public void setRecruitEndTime(final Date pRecruitEndTime) {
        this.recruitEndTime = pRecruitEndTime;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(final String pMerchantCode) {
        this.merchantCode = pMerchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(final String pMerchantName) {
        this.merchantName = pMerchantName;
    }

    public String getMerchantBank() {
        return merchantBank;
    }

    public void setMerchantBank(final String pMerchantBank) {
        this.merchantBank = pMerchantBank;
    }

    public String getMerchantBankNo() {
        return merchantBankNo;
    }

    public void setMerchantBankNo(final String pMerchantBankNo) {
        this.merchantBankNo = pMerchantBankNo;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(final String pVenderCode) {
        this.venderCode = pVenderCode;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(final String pVenderName) {
        this.venderName = pVenderName;
    }

    public String getVenderBank() {
        return venderBank;
    }

    public void setVenderBank(final String pVenderBank) {
        this.venderBank = pVenderBank;
    }

    public String getVenderBankNo() {
        return venderBankNo;
    }

    public void setVenderBankNo(final String pVenderBankNo) {
        this.venderBankNo = pVenderBankNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final Date pUpdateTime) {
        this.updateTime = pUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final Date pCreateTime) {
        this.createTime = pCreateTime;
    }
}
