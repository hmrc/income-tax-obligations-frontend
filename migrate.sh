#!/usr/bin/env bash

echo "Starting cut over of main app from income-tax-view-change-frontend"
mkdir tmp

echo "Copying files to tmp folder which we don't want to overwrite"
cp -r app/common/config/ExternalRedirectHelper.scala tmp/.
cp -r app/common/config/FrontendAppConfig.scala tmp/.
cp app/common/models/liabilitycalculation/LiabilityCalculationResponse.scala tmp/.
cp app/shared/models/UIJourneySessionData.scala tmp/.
cp app/common/models/obligations/ObligationsResponseModel.scala tmp/.
cp app/common/models/incomeSourceDetails/IncomeSourceDetailsResponse.scala tmp/.
cp -r app/shared/enums/journeyType tmp/.
cp app/common/connectors/FeatureSwitchConnector.scala tmp/.
cp cp app/common/models/admin/FeatureSwitchName.scala tmp/.
cp app/common/services/admin/FeatureSwitchService.scala tmp/.

echo "Removing main code from the app folder"
rm -rf app/common
rm -rf app/shared
rm -rf app/obligations

echo "Copying main code from the income-tax-view-change-frontend to app folder"

cp -r ../income-tax-view-change-frontend/app/common app/.
cp -r ../income-tax-view-change-frontend/app/shared app/.
cp -r ../income-tax-view-change-frontend/app/obligations app/.

echo "Copying files back from tmp folder to app folder"
cp tmp/ExternalRedirectHelper.scala app/common/config/.
cp tmp/FrontendAppConfig.scala app/common/config/.
cp tmp/LiabilityCalculationResponse.scala app/common/models/liabilitycalculation/.
cp tmp/UIJourneySessionData.scala app/shared/models/.
cp tmp/ObligationsResponseModel.scala app/common/models/obligations/.
cp tmp/IncomeSourceDetailsResponse.scala app/common/models/incomeSourceDetails/.
cp -r tmp/journeyType app/shared/enums/.
cp tmp/FeatureSwitchConnector.scala app/common/connectors/.
cp tmp/FeatureSwitchName.scala app/common/models/admin/.
cp tmp/FeatureSwitchService.scala app/common/services/admin/.

echo "Removing unused files"
rm app/shared/implicits/ImplicitCurrencyFormatter.scala
rm app/shared/enums/CodingOutType.scala
rm app/shared/enums/DocumentType.scala
rm app/shared/utils/Utilities.scala

echo "Removing files from tmp folder"
rm -rf tmp/*

echo "Starting cut over of unit tests from income-tax-view-change-frontend"
echo "Copying files to tmp folder which we don't want to overwrite"

cp test/common/models/liabilityCalculation/LiabilityCalculationResponseModelSpec.scala tmp/.
cp test/obligations/models/ObligationsResponseModelSpec.scala tmp/.
cp test/common/models/IncomeSourceDetailsModelSpec.scala tmp/.
cp test/common/config/featureswitch/FeatureSwitchingSpec.scala tmp/.
cp test/common/admin/FeatureSwitchNameSpec.scala tmp/.
cp test/common/services/admin/FeatureSwitchServiceSpec.scala tmp/.
cp test/common/mocks/connectors/MockFeatureSwitchConnector.scala tmp/.

echo "Removing current unit tests"

rm -rf test/common
rm -rf test/shared
rm -rf test/obligations

echo "Copying unit tests from the income-tax-view-change-frontend"

cp -r ../income-tax-view-change-frontend/test/common test/.
cp -r ../income-tax-view-change-frontend/test/shared test/.
cp -r ../income-tax-view-change-frontend/test/obligations test/.
cp -r ../income-tax-view-change-frontend/test/resources test/.

echo "Copying files back from tmp folder to test folder"

cp tmp/LiabilityCalculationResponseModelSpec.scala test/common/models/liabilityCalculation/.
cp tmp/ObligationsResponseModelSpec.scala test/obligations/models/.
cp tmp/IncomeSourceDetailsModelSpec.scala test/common/models/.
cp tmp/FeatureSwitchingSpec.scala test/common/config/featureswitch/.
cp tmp/FeatureSwitchNameSpec.scala test/common/admin/.
cp tmp/FeatureSwitchServiceSpec.scala test/common/services/admin/.
cp tmp/MockFeatureSwitchConnector.scala test/common/mocks/connectors/.

echo "Removing unused files"
rm test/common/models/liabilityCalculation/MetadataSpec.scala

echo "Removing files from tmp folder"
rm tmp/LiabilityCalculationResponseModelSpec.scala

echo "Starting cut over of integration tests from income-tax-view-change-frontend"
echo "Copying files to tmp folder which we don't want to overwrite"

cp it/test/common/helpers/ComponentSpecBase.scala tmp/.
cp it/test/common/controllers/ControllerISpecBase.scala tmp/.
cp it/test/common/testConstants/MicroserviceSpecificConstants.scala tmp/.

echo "Removing current integration tests"

rm -rf it/test/common
rm -rf it/test/shared
rm -rf it/test/obligations

echo "Copying integration tests from the income-tax-view-change-frontend"
cp -r ../income-tax-view-change-frontend/it/test/common it/test/.
cp -r ../income-tax-view-change-frontend/it/test/obligations it/test/.
cp -r ../income-tax-view-change-frontend/it/test/shared it/test/.

echo "Copying files back from tmp folder to test folder"
cp tmp/ComponentSpecBase.scala it/test/common/helpers/.
cp tmp/ControllerISpecBase.scala it/test/common/controllers/.
cp tmp/MicroserviceSpecificConstants.scala it/test/common/testConstants/.

echo "Removing tmp folder"
rm -rf tmp

echo "Cut over complete"

echo "running all tests to ensure everything is working as expected"

./run_all_tests.sh

