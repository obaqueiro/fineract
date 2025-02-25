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
package org.apache.fineract.portfolio.delinquency.service;

import java.time.LocalDate;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.portfolio.loanaccount.data.LoanScheduleDelinquencyData;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;

public interface DelinquencyWritePlatformService {

    CommandProcessingResult createDelinquencyRange(JsonCommand command);

    CommandProcessingResult updateDelinquencyRange(Long delinquencyRangeId, JsonCommand command);

    CommandProcessingResult deleteDelinquencyRange(Long delinquencyRangeId, JsonCommand command);

    CommandProcessingResult createDelinquencyBucket(JsonCommand command);

    CommandProcessingResult updateDelinquencyBucket(Long delinquencyBucketId, JsonCommand command);

    CommandProcessingResult deleteDelinquencyBucket(Long delinquencyBucketId, JsonCommand command);

    CommandProcessingResult applyDelinquencyTagToLoan(Long loanId, JsonCommand command);

    void removeDelinquencyTagToLoan(Loan loan);

    void cleanLoanDelinquencyTags(Loan loan);

    LoanScheduleDelinquencyData calculateDelinquencyData(LoanScheduleDelinquencyData loanScheduleDelinquencyData);

    void applyDelinquencyTagToLoan(LoanScheduleDelinquencyData loanDelinquencyData);

    LocalDate getOverdueSinceDate(Loan loan, LocalDate businessDate, Integer graceOnArrearAgeing);

}
