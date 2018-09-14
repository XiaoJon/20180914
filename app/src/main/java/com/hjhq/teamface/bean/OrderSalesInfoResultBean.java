package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 订单销售情况返回实体
 *
 * @author xj
 * @date 2017/10/20
 */

public class OrderSalesInfoResultBean extends BaseBean {

    /**
     * data : {"projectInfo":[{"schedule":50,"projectName":"销售华企秘书500套","projectId":"1"}],"invoiceInfo":[{"invoiceMoney":5000,"invoiceId":1,"invoiceNo":"FP-20170509-174725","invoiceDate":1508465097458}],"proceedsInfo":[{"proceedsDate":1508465097458,"proceedsNo":"HK-20170509-174725","proceedsMoney":5000,"proceedsId":1,"proceedsCount":"HK-20170509-174726"},{"proceedsDate":1508465097458,"proceedsMoney":6000,"proceedsCount":"HK-20170509-174726","proceedsId":2}],"contractInfo":[{"contractDate":1508465097458,"contractNo":"HT20170419181223","contractId":"2","contractMoney":5000}],"orderInfo":[{"orderAmount":5000,"nonProceedsAmount":5000,"proceedsAmount":5000}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private ProjectInfoBean projectInfo;
        private InvoiceInfoBean invoiceInfo;
        private ProceedsInfoBean proceedsInfo;
        private ContractInfoBean contractInfo;
        private OrderInfoBean orderInfo;


        public InvoiceInfoBean getInvoiceInfo() {
            return invoiceInfo;
        }

        public void setInvoiceInfo(InvoiceInfoBean invoiceInfo) {
            this.invoiceInfo = invoiceInfo;
        }

        public ProceedsInfoBean getProceedsInfo() {
            return proceedsInfo;
        }

        public void setProceedsInfo(ProceedsInfoBean proceedsInfo) {
            this.proceedsInfo = proceedsInfo;
        }

        public ContractInfoBean getContractInfo() {
            return contractInfo;
        }

        public void setContractInfo(ContractInfoBean contractInfo) {
            this.contractInfo = contractInfo;
        }

        public ProjectInfoBean getProjectInfo() {
            return projectInfo;
        }

        public void setProjectInfo(ProjectInfoBean projectInfo) {
            this.projectInfo = projectInfo;
        }

        public OrderInfoBean getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfoBean orderInfo) {
            this.orderInfo = orderInfo;
        }

        public static class ProjectInfoBean {
            /**
             * schedule : 50
             * projectName : 销售华企秘书500套
             * projectId : 1
             */

            private int schedule;
            private String projectName;
            private String currentStep;
            private Long projectId;

            public int getSchedule() {
                return schedule;
            }

            public void setSchedule(int schedule) {
                this.schedule = schedule;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public Long getProjectId() {
                return projectId;
            }

            public void setProjectId(Long projectId) {
                this.projectId = projectId;
            }

            public String getCurrentStep() {
                return currentStep;
            }

            public void setCurrentStep(String currentStep) {
                this.currentStep = currentStep;
            }
        }

        public static class InvoiceInfoBean {
            private List<InvoiceList> invoiceList;
            private int invoiceCount;
            public static class InvoiceList {
                /**
                 * invoiceMoney : 5000
                 * invoiceId : 1
                 * invoiceNo : FP-20170509-174725
                 * invoiceDate : 1508465097458
                 */

                private String invoiceMoney;
                private String invoiceId;
                private String invoiceNo;
                private Long invoiceDate;

                public String getInvoiceMoney() {
                    return invoiceMoney;
                }

                public void setInvoiceMoney(String invoiceMoney) {
                    this.invoiceMoney = invoiceMoney;
                }

                public String getInvoiceId() {
                    return invoiceId;
                }

                public void setInvoiceId(String invoiceId) {
                    this.invoiceId = invoiceId;
                }

                public String getInvoiceNo() {
                    return invoiceNo;
                }

                public void setInvoiceNo(String invoiceNo) {
                    this.invoiceNo = invoiceNo;
                }

                public Long getInvoiceDate() {
                    return invoiceDate;
                }

                public void setInvoiceDate(Long invoiceDate) {
                    this.invoiceDate = invoiceDate;
                }
            }

            public List<InvoiceList> getInvoiceList() {
                return invoiceList;
            }

            public void setInvoiceList(List<InvoiceList> invoiceList) {
                this.invoiceList = invoiceList;
            }

            public int getInvoiceCount() {
                return invoiceCount;
            }

            public void setInvoiceCount(int invoiceCount) {
                this.invoiceCount = invoiceCount;
            }
        }


        public static class ProceedsInfoBean {
            private int proceedsCount;
            private List<ProceedsList> proceedsList;

            public static class ProceedsList {
                /**
                 * proceedsDate : 1508465097458
                 * proceedsNo : HK-20170509-174725
                 * proceedsMoney : 5000
                 * proceedsId : 1
                 * proceedsCount : HK-20170509-174726
                 */

                private Long proceedsDate;
                private String proceedsNo;
                private String proceedsMoney;
                private String proceedsId;
                private String proceedsCount;

                public Long getProceedsDate() {
                    return proceedsDate;
                }

                public void setProceedsDate(Long proceedsDate) {
                    this.proceedsDate = proceedsDate;
                }

                public String getProceedsNo() {
                    return proceedsNo;
                }

                public void setProceedsNo(String proceedsNo) {
                    this.proceedsNo = proceedsNo;
                }

                public String getProceedsMoney() {
                    return proceedsMoney;
                }

                public void setProceedsMoney(String proceedsMoney) {
                    this.proceedsMoney = proceedsMoney;
                }

                public String getProceedsId() {
                    return proceedsId;
                }

                public void setProceedsId(String proceedsId) {
                    this.proceedsId = proceedsId;
                }

                public String getProceedsCount() {
                    return proceedsCount;
                }

                public void setProceedsCount(String proceedsCount) {
                    this.proceedsCount = proceedsCount;
                }
            }

            public int getProceedsCount() {
                return proceedsCount;
            }

            public void setProceedsCount(int proceedsCount) {
                this.proceedsCount = proceedsCount;
            }

            public List<ProceedsList> getProceedsList() {
                return proceedsList;
            }

            public void setProceedsList(List<ProceedsList> proceedsList) {
                this.proceedsList = proceedsList;
            }
        }


        public static class ContractInfoBean {
            private List<ContractList> contractList;
            private int contractCount;

            public static class ContractList {
                /**
                 * contractDate : 1508465097458
                 * contractNo : HT20170419181223
                 * contractId : 2
                 * contractMoney : 5000
                 */

                private Long contractDate;
                private String contractNo;
                private String contractId;
                private String contractMoney;

                public Long getContractDate() {
                    return contractDate;
                }

                public void setContractDate(Long contractDate) {
                    this.contractDate = contractDate;
                }

                public String getContractNo() {
                    return contractNo;
                }

                public void setContractNo(String contractNo) {
                    this.contractNo = contractNo;
                }

                public String getContractId() {
                    return contractId;
                }

                public void setContractId(String contractId) {
                    this.contractId = contractId;
                }

                public String getContractMoney() {
                    return contractMoney;
                }

                public void setContractMoney(String contractMoney) {
                    this.contractMoney = contractMoney;
                }
            }

            public List<ContractList> getContractList() {
                return contractList;
            }

            public void setContractList(List<ContractList> contractList) {
                this.contractList = contractList;
            }

            public int getContractCount() {
                return contractCount;
            }

            public void setContractCount(int contractCount) {
                this.contractCount = contractCount;
            }
        }


        public static class OrderInfoBean {

            /**
             * nonProceedsMoney : -34434
             * orderMoney : 6789
             * proceedsMoney : 41223
             */

            private String nonProceedsMoney;
            private String orderMoney;
            private String proceedsMoney;

            public String getNonProceedsMoney() {
                return nonProceedsMoney;
            }

            public void setNonProceedsMoney(String nonProceedsMoney) {
                this.nonProceedsMoney = nonProceedsMoney;
            }

            public String getOrderMoney() {
                return orderMoney;
            }

            public void setOrderMoney(String orderMoney) {
                this.orderMoney = orderMoney;
            }

            public String getProceedsMoney() {
                return proceedsMoney;
            }

            public void setProceedsMoney(String proceedsMoney) {
                this.proceedsMoney = proceedsMoney;
            }
        }
    }
}
