package net.rmitsolutions.mfexpert.lms.dependency.components

import dagger.Component
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ActivityScope
import net.rmitsolutions.mfexpert.lms.sync.banks.BankSyncService
import net.rmitsolutions.mfexpert.lms.sync.caste.CasteDetailSyncService
import net.rmitsolutions.mfexpert.lms.sync.district.DistrictSyncService
import net.rmitsolutions.mfexpert.lms.sync.houseownership.HouseOwnershipSyncService
import net.rmitsolutions.mfexpert.lms.sync.incomeproof.IncomeProofSyncService
import net.rmitsolutions.mfexpert.lms.sync.literacy.LiteracySyncService
import net.rmitsolutions.mfexpert.lms.sync.loanpurpose.LoanPurposeSyncService
import net.rmitsolutions.mfexpert.lms.sync.occupation.OccupationSyncService
import net.rmitsolutions.mfexpert.lms.sync.primarykyc.PrimaryKycSyncService
import net.rmitsolutions.mfexpert.lms.sync.relations.RelationSyncService
import net.rmitsolutions.mfexpert.lms.sync.religion.ReligionSyncService
import net.rmitsolutions.mfexpert.lms.sync.secondarykyc.SecondaryKycSyncService

/**
 * Created by Madhu on 24-Apr-2018.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectServiceComponent {
    //fun injectFileDownloadService(service: FileDownloadService)

    fun injectDistrictSyncService(districtSyncService: DistrictSyncService)
    fun injectRelationSyncService(relationSyncService: RelationSyncService)
    fun injectOccupationSyncService(occupationSyncService: OccupationSyncService)
    fun injectLiteracySyncService(literacySyncService: LiteracySyncService)
    fun injectPrimaryKycSyncService(primaryKycSyncService: PrimaryKycSyncService)
    fun injectSecondarySyncService(secondaryKycSyncService: SecondaryKycSyncService)
    fun injectLoanPurposeSyncService(loanPurposeSyncService: LoanPurposeSyncService)
    fun injectBankSyncService(bankSyncService: BankSyncService)
    fun injectCasteSyncService(casteSyncService: CasteDetailSyncService)
    fun injectHouseOwnershipSyncService(houseOwnershipSyncService: HouseOwnershipSyncService)
    fun injectIncomeProofSyncService(incomeProofSyncService: IncomeProofSyncService)
    fun injectReligionSyncService(religionSyncService : ReligionSyncService)
}