/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cob.loan;

import java.time.LocalDate;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.configuration.domain.ConfigurationDomainService;
import org.apache.fineract.infrastructure.core.service.DateUtils;
import org.apache.fineract.infrastructure.event.business.domain.loan.LoanRepaymentOverdueBusinessEvent;
import org.apache.fineract.infrastructure.event.business.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.loanschedule.data.OverdueLoanScheduleData;
import org.apache.fineract.portfolio.loanaccount.service.LoanReadPlatformService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLoanRepaymentOverdueBusinessStep implements LoanCOBBusinessStep {

    private final LoanReadPlatformService loanReadPlatformService;
    private final ConfigurationDomainService configurationDomainService;
    private final BusinessEventNotifierService businessEventNotifierService;

    @Override
    public Loan execute(Loan loan) {
        Long numberOfDaysAfterDueDateToRaiseEvent = configurationDomainService.retrieveRepaymentOverdueDays();
        final LocalDate currentDate = DateUtils.getBusinessLocalDate();
        final Collection<OverdueLoanScheduleData> overdueInstallmentsForLoan = loanReadPlatformService
                .retrieveAllOverdueInstallmentsForLoan(loan);
        for (OverdueLoanScheduleData overdueInstallment : overdueInstallmentsForLoan) {
            LocalDate installmentDueDate = LocalDate.parse(overdueInstallment.getDueDate());
            if (installmentDueDate.plusDays(numberOfDaysAfterDueDateToRaiseEvent).equals(currentDate)) {
                businessEventNotifierService.notifyPostBusinessEvent(new LoanRepaymentOverdueBusinessEvent(loan));
                break;
            }
        }
        return loan;
    }

    @Override
    public String getEnumStyledName() {
        return "CHECK_LOAN_REPAYMENT_OVERDUE";
    }

    @Override
    public String getHumanReadableName() {
        return "Check loan repayment overdue";
    }
}
