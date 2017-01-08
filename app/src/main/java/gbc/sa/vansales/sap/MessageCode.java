package gbc.sa.vansales.sap;
import java.util.HashMap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
enum MessageCode {
    OKAY(0, 0, 0, "[0] Okay."),
    DEBUG_EXCEPTION(-10000, 3, 19, "[-10000] Debug exception: %s"),
    ERROR_EXECUTING_SQL_STMT(-10001, 3, 19, "[-10001] An error occurred executing SQL statement: %s.  Reason: %s."),
    ERROR_CONNECTING_TO_DB(-10005, 3, 15, "[-10005] An error occurred connecting to the database using connection string \"%s\".  Reason: %s"),
    DB_NOT_FOUND(-10006, 3, 15, "[-10006] An error occurred connecting to the database using connection string \"%s\".  Reason: %s"),
    ERROR_LOADING_EDM_METADATA(-10010, 3, 19, "[-10010] An error occurred while loading the OData metadata."),
    ERROR_LOADING_MAPPING(-10016, 3, 19, "[-10016] An error occurred while loading the mapping from OData metadata to database metadata."),
    FATAL_PARSE_ERROR(-10021, 1, 5, "[-10021] Error at URL position %lu: fatal parsing error"),
    URL_SYNTAX_ERROR(-10022, 3, 5, "[-10022] Error at URL position %lu: \"%s\" was unexpected at this point."),
    UNEXPECTED_EOI(-10023, 3, 5, "[-10023] Prematurely reached the end of the request URL"),
    ILLEGAL_TYPE_VALUE(-10024, 3, 5, "[-10024] Error at URL position %lu: \"%s\" is an illegal %s"),
    ILLEGAL_USE_OF_KEYWORD(-10025, 3, 5, "[-10025] Error at URL position %lu: illegal use of keyword \"%s\""),
    EMPTY_RESOURCE_PATH(-10026, 3, 2, "[-10026] The request is invalid because there is no resource path."),
    INVALID_ENTITY_CONTAINER(-10027, 3, 5, "[-10027] Error at URL position %lu: \"%s\" is an invalid entity container name"),
    INVALID_ENTITY_SET(-10028, 3, 5, "[-10028] Error at URL position %lu: \"%s\" is not an entity set in entity container \"%s\""),
    INVALID_KEY_PREDICATE(-10029, 3, 5, "[-10029] Error at URL position %lu: a key predicate can only follow a resource path which identifies a collection of entities."),
    INVALID_UNNAMED_KEY_VALUE(-10030, 3, 5, "[-10030] Error at URL position %lu: invalid unnamed key value appears.  Unnamed key values are only allowed for key predicates of entity types with a single key property."),
    WRONG_NUMBER_OF_KEY_VALUES(-10031, 3, 5, "[-10031] Error at URL position %lu: wrong number of key values for entity type \"%s.%s\"."),
    INVALID_KEY_PROPERTY(-10032, 3, 5, "[-10032] Error at URL position %lu: \"%s\" is not a key property of entity type \"%s.%s\"."),
    DUPLICATE_KEY_PROPERTY(-10033, 3, 5, "[-10033] Error at URL position %lu: key property \"%s\" appears more than once in the key predicate"),
    INVALID_KEY_PROPERTY_TYPE(-10034, 3, 5, "[-10034] Error at URL position %lu: invalid key property type for \"%s\"; expected \"%s\", received \"%s\"."),
    INVALID_NULL_IN_KEY_PREDICATE(-10035, 3, 5, "[-10035] Error at URL position %lu: invalid null literal in a key predicate"),
    INVALID_PROPERTY(-10036, 3, 5, "[-10036] Error at URL position %lu: \"%s\" is not a property of \"%s.%s\"."),
    INVALID_EMPTY_PAREN(-10037, 3, 5, "[-10037] Error at URL position %lu: \"()\" is invalid for this path segment."),
    INVALID_PATH_SEGMENT_AFTER_PROP(-10038, 3, 5, "[-10038] Error at URL position %lu: invalid path segment following a property."),
    INVALID_PATH_SEGMENT_AFTER_ESET(-10039, 3, 5, "[-10039] Error at URL position %lu: invalid path segment following an entity set."),
    ERROR_SETTING_NULL_PARAMETER(-10046, 3, 19, "[-10046] An error occurred while setting a NULL parameter in a prepared statement"),
    ERROR_SETTING_PARAMETER(-10047, 3, 19, "[-10047] An error occurred while setting a parameter with an %s value in a prepared statement"),
    DUPLICATE_QUERY_OPTION(-10048, 3, 5, "[-10048] Error at URL position %lu: system query option \"%s\" is used more than once in the URL"),
    NEGATIVE_TOP(-10049, 3, 5, "[-10049] Error at URL position %lu: the value associated with $top is negative"),
    NEGATIVE_SKIP(-10050, 3, 5, "[-10050] Error at URL position %lu: the value associated with $skip is negative"),
    ORDER_BY_MUST_END_WITH_PROPERTY(-10051, 3, 5, "[-10051] Error at URL position %lu: $orderby expressions must end with a simple property"),
    TOP_TOO_LARGE(-10052, 3, 5, "[-10052] Error at URL position %lu: the value associated with $top is too large"),
    SKIP_TOO_LARGE(-10053, 3, 5, "[-10053] Error at URL position %lu: the value associated with $skip is too large"),
    ERROR_LOADING_DB_METADATA(-10054, 3, 19, "[-10054] An error occurred while loading the database metadata."),
    STORE_ALREADY_OPEN(-10055, 3, 11, "[-10055] Cannot open the store because it is already open."),
    STORE_PREVIOUSLY_CLOSED(-10056, 3, 12, "[-10056] Cannot open the store because it has been previously closed."),
    ERROR_READING_METADATA_FROM_DB(-10057, 3, 19, "[-10057] An error occurred while reading the metadata from the store."),
    STORE_NOT_OPEN(-10058, 3, 10, "[-10058] Cannot perform the requested operation because the store is not open."),
    ERROR_EXECUTING_GET_REQUEST(-10059, 3, 19, "[-10059] An error occurred while executing a read request.  Reason: %s."),
    ERROR_SYNCHRONIZING(-10060, 2, 2, "[-10060] An error occurred while performing a synchronization.  Reason: %s"),
    ERROR_EXPANDING_DEPLOY_FILE(-10061, 3, 19, "[-10061] An error occurred while expanding the deploy file received from the server.  Reason: %s"),
    ERROR_INITIALIZING_STORE(-10062, 2, 2, "[-10062] An error occurred while initializing the store."),
    UNSUPPORTED_KEY_WORD(-10063, 3, 5, "[-10063] Error at URL position %lu: \"%s\" is not a supported keyword"),
    UNABLE_TO_DELETE_DB(-10064, 3, 19, "[-10064] The template database file located at \"%s\" could not be deleted."),
    DOWNLOAD_DB_FAILED(-10065, 2, 3, "[-10065] Downloading the store failed.  Reason: Error %ld (%s) (System code %ld)"),
    ERROR_VALIDATING_DB(-10066, 2, 2, "[-10066] An error occurred while validating the store."),
    COULD_NOT_CREATE_DB(-10067, 3, 19, "[-10067] Could not create the store.  Reason: %s."),
    INVALID_PROPERTY_VALUE_TYPE(-10069, 3, 16, "[-10069] An invalid value type for property \"%s\" was provided. A \"%s\" was expected."),
    INVALID_USE_OF_EXPAND(-10070, 3, 5, "[-10070] Error at URL Position %lu: the $expand system query option can only be used when retrieving an entity set or entity type"),
    INVALID_NAVIGATION_PROPERTY(-10071, 3, 5, "[-10071] Error at URL Position %lu: \"%s\" is not a navigation property of \"%s.%s\""),
    INVALID_USE_OF_SELECT(-10072, 3, 5, "[-10072] Error at URL Position %lu: the $select system query option can only be used when retrieving an entity set, entity type, or complex type"),
    SELECT_OF_NON_EXPANDED_ITEM(-10073, 3, 5, "[-10073] Error at URL Position %lu: \"%s\" is selected but is not an expanded navigation property of \"%s.%s\""),
    FOLLOWED_PROPERTY_IN_SELECT(-10074, 3, 5, "[-10074] Error at URL Position %lu: a simple property cannot be followed by a path expression in a $select clause"),
    INVALID_USE_OF_TOP(-10075, 3, 5, "[-10075] Error at URL Position %lu: the $top system query option can only be used when retrieving a collection"),
    INVALID_USE_OF_SKIP(-10076, 3, 5, "[-10076] Error at URL Position %lu: the $skip system query option can only be used when retrieving a collection"),
    ORDER_BY_OF_NON_EXPANDED_ITEM(-10077, 3, 5, "[-10077] Error at URL Position %lu: \"%s\" appears in $orderby but is not an expanded navigation property of \"%s.%s\""),
    INVALID_USE_OF_ORDER_BY(-10078, 3, 5, "[-10078] Error at URL Position %lu: the $orderby system query option can only be used when retrieving a collection or when retrieving a single entity with an $expand"),
    ERROR_PREPARING_SQL_STMT(-10080, 3, 19, "[-10080] An error occurred while preparing SQL statement \"%s\": %s"),
    ERROR_STMT_NOT_PREPARED(-10081, 3, 19, "[-10081] An error occurred while attempting to execute the following SQL statement which was not prepared: \"%s\""),
    INVALID_USE_OF_FILTER(-10082, 3, 5, "[-10082] Error at URL Position %lu: the $filter system query option can only be used when retrieving a collection"),
    FILTER_MUST_BE_BOOLEAN(-10083, 3, 5, "[-10083] The $filter system query option must evaluate to a boolean value"),
    INVALID_ARITHMETIC_TYPE(-10084, 3, 5, "[-10084] Error at URL Position %lu: \"%s\" is not an arithmetic type"),
    TYPES_NOT_PROMOTABLE(-10085, 3, 5, "[-10085] There is no automatic promotion of EDM types \"%s\" and \"%s\""),
    GENERIC_FILTER_ERROR(-10086, 3, 5, "[-10086] Error at URL Position %lu: %s"),
    TYPES_NOT_COMPARABLE(-10087, 3, 5, "[-10087] Error at URL Position %lu: EDM types \"%s\" and \"%s\" are not comparable"),
    BOOLEAN_RELATION_OP_ERROR(-10088, 3, 5, "[-10088] Error at URL Position %lu: Edm.Boolean types are not comparable using operator \"%s\""),
    INVALID_BOOLEAN_TYPE(-10089, 3, 5, "[-10089] Error at URL Position %lu: \"%s\" is not a boolean type"),
    INVALID_EDM_TYPE_NAME(-10090, 3, 16, "[-10090] \"%s\" is not a valid EDM type"),
    UNSUPPORTED_METHOD_CALL(-10091, 3, 4, "[-10091] Error at URL Position %lu: \"%s\" is not a supported method"),
    UNKNOWN_METHOD_CALL(-10092, 3, 5, "[-10092] Error at URL Position %lu: \"%s\" is an unknown method call"),
    INVALID_PARAMETER_TYPE(-10093, 3, 5, "[-10093] Error at URL Position %lu: expected a parameter that evaluates to \"%s\""),
    INVALID_PARAMETER_COUNT(-10094, 3, 5, "[-10094] Error at URL Position %lu: method \"%s\" does not take %lu parameters"),
    CAST_TO_BOOLEAN(-10095, 3, 5, "[-10095] Error at URL Position %lu: cast to Edm.Boolean is not supported"),
    INVALID_MEMBER_EXPRESSION_END(-10096, 3, 5, "[-10096] Error at URL Position %lu: a member expression in $filter must end with a simple property"),
    INVALID_NAV_PROP_MULTIPLICITY(-10097, 3, 5, "[-10097] Error at URL Position %lu: navigation property \"%s\" of \"%s.%s\" cannot be used as a member expression in $filter because its end cardinality is *"),
    INVALID_CAST_EDM_TYPE(-10098, 3, 5, "[-10098] Error at URL Position %lu: \"%s\" is an invalid EDM type"),
    INVALID_PATH_SEGMENT_AFTER_CTYPE(-10099, 3, 5, "[-10099] Error at URL position %lu: invalid path segment following a complex type property"),
    INVALID_PATH_SEGMENT_AFTER_ETYPE(-10100, 3, 5, "[-10100] Error at URL position %lu: invalid path segment following an entity type"),
    NO_METADATA_DOC(-10101, 3, 19, "[-10101] The metadata document could not be loaded"),
    INVALID_BINARY_OP_OPERAND_TYPE(-10102, 3, 5, "[-10102] Error at URL Position %lu: the $filter system query option can only be used when retrieving a collection"),
    ERROR_EXECUTING_MOD_REQUEST(-10103, 3, 19, "[-10103] An error occurred while executing a modification request.  Reason: %s"),
    ERROR_COMMITTING_DB_TRANSACTION(-10104, 3, 19, "[-10104] An error occurred while committing a database transaction.  Reason: %s"),
    ERROR_GENERATING_ENTITY_ID(-10105, 3, 19, "[-10105] An error occurred while generating a local entity id value."),
    INVALID_LINK_ADDRESS(-10106, 3, 999, "[-10106] Invalid link address: \"%s\"."),
    ERROR_UPDATE_ENTITY_NOT_EXIST(-10107, 3, 999, "[-10107] The update entity request failed because the provided entity does not exist."),
    INVALID_ENTITY_URL(-10108, 3, 5, "[-10108] Error at URL Position %lu: this is not a valid entity type instance URL."),
    NO_SERVICE_DOC(-10110, 3, 19, "[-10110] The service document could not be loaded"),
    FATAL_INTERNAL_ERROR(-10111, 3, 19, "[-10111] A fatal internal error has occurred"),
    INVALID_PATH_SEGMENT_AFTER_LINKS(-10112, 3, 5, "[-10112] Error at URL position %lu: invalid path segment following a $links segment"),
    INVALID_PATH_FOR_CREATE_ENTITY(-10113, 3, 5, "[-10113] The resource path for a create entity request must address an entity set"),
    INVALID_PATH_FOR_MODIFY_ENTITY(-10114, 3, 5, "[-10114] The resource path for a modify entity request must address an entity type instance"),
    INVALID_PATH_FOR_MODIFY_LINK(-10115, 3, 5, "[-10115] The resource path for a modify links request must address the link between two entity type instances"),
    INVALID_PATH_FOR_MODIFY_LINKS(-10116, 3, 5, "[-10116] The resource path for a create link request must address a navigation property that refers to an entity set"),
    INVALID_PATH_FOR_MODIFY_PROPERTY(-10117, 3, 5, "[-10117] The resource path for a modify property request must address a property of an entity type instance"),
    INVALID_PATH_FOR_MODIFY_VALUE(-10118, 3, 5, "[-10118] The resource path for a modify property value request must address the value of an entity type instance property"),
    ERROR_UPDATE_ENTITY_WRONG_TYPE(-10119, 3, 999, "[-10119] The modify entity request failed because the provided entity was not the right type for the entity set"),
    ERROR_ADDING_LINK_WRONG_TYPE(-10120, 3, 999, "[-10120] The update link request failed because an entity of type \"%s.%s\" was expected but an entity of type \"%s.%s\" was provided"),
    ERROR_ADDING_LINK_ALREADY_EXISTS(-10121, 3, 999, "[-10121] The update link request failed because a link already exists between the provided entities"),
    INVALID_USE_OF_INLINECOUNT(-10124, 3, 5, "[-10124] Error at URL Position %lu: the $inlinecount system query option can only be used when retrieving a collection"),
    CANNOT_DELETE_PRIMARY_ENTITY(-10125, 3, 999, "[-10125] Cannot delete this entity instance.  Another entity instance depends on it."),
    ERROR_GENERATING_ETAG(-10127, 3, 19, "[-10127] An error occurred while generating an ETag value."),
    ERROR_ETAGS_DIFFER(-10128, 3, 999, "[-10128] The request on an entity failed because its etag did not match the request\'s conditions."),
    LINK_SOURCE_NOT_EXIST(-10129, 3, 5, "[-10129] The modify link request failed because the source entity does not exist."),
    LINK_LINKED_NOT_EXIST(-10130, 3, 5, "[-10130] The modify link request failed because the linked entity does not exist."),
    MODIFY_LINK_VIOLATES_CARDINALITY(-10131, 3, 5, "[-10131] A modify link request failed because the modification violated the cardinality of the association"),
    INVALID_REFRESH_SUBSET(-10132, 3, 15, "[-10132] The provided refresh subset string is invalid"),
    DEFINING_REQUEST_NOT_FOUND(-10133, 3, 15, "[-10133] A defining request with name \"%s\" does not exist"),
    INVALID_DEFINING_REQUEST(-10134, 3, 13, "[-10134] An invalid defining request was provided. Request name: \"%s\". Request value: \"%s\""),
    REQUEST_NAME_TOO_LONG(-10135, 3, 13, "[-10135] A name provided for a defining request exceeded the maximum allowable length of %lu: \"%s\""),
    INVALID_ENTITY_EXISTS_PARAMETER(-10136, 3, 5, "[-10136] Error at URL Position %lu: sap.entityexists expects a member expression which consists of navigation properties with end cardinality 1 or 0..1"),
    SHIM_STORE_OPTIONS_NOT_PROVIDED(-10137, 3, 14, "[-10137] Error opening the store. No store options were provided."),
    MISSING_SERVICE_ROOT(-10138, 3, 13, "[-10138] Error opening the store. No service root was provided."),
    INVALID_PAGE_SIZE(-10139, 3, 13, "[-10139] Error opening the store. The page size is not valid."),
    MISSING_ML_STREAM_PARMS(-10140, 3, 13, "[-10140] Error opening the store. No MobiLink stream parameters were provided."),
    MISSING_USERNAME(-10141, 3, 13, "[-10141] Error opening the store. A password was provided without also providing a username."),
    MISSING_PASSWORD(-10142, 3, 13, "[-10142] Error opening the store. A username was provided without also providing a password."),
    MISSING_STORE_NAME(-10143, 3, 13, "[-10143] Error opening the store. No store name was provided."),
    MISSING_STORE_PATH(-10144, 3, 13, "[-10144] Error opening the store. No store path was provided."),
    SHIM_MISSING_REQUEST(-10145, 3, 13, "[-10145] The request was not provided."),
    REQUEST_TYPE_NOT_SUPPORTED(-10146, 3, 7, "[-10146] This type of request is not supported."),
    MISSING_PAYLOAD(-10147, 3, 999, "[-10147] The modification request payload is missing."),
    INVALID_PAYLOAD_TYPE(-10148, 3, 6, "[-10148] The provided payload is invalid or not supported for a modification request."),
    DROP_DB_NOT_FOUND(-10149, 3, 15, "[-10149] The store could not be dropped because it was not found."),
    COULD_NOT_DROP_DB(-10150, 3, 19, "[-10150] Could not drop the store.  Reason: %s."),
    ERROR_COMPUTING_BASIC_AUTH(-10151, 3, 19, "[-10151] An error occurred while computing the basic authentication http header"),
    INVALID_USE_OF_SKIP_TOKEN(-10152, 3, 5, "[-10152] Error at URL Position %lu: the $skiptoken system query option can only be used when retrieving a collection"),
    INVALID_SKIP_WITH_SKIP_TOKEN(-10153, 3, 5, "[-10153] Error at URL Position %lu: $skip cannot be used with $skiptoken"),
    INVALID_SKIP_TOKEN_ITEM_COUNT(-10154, 3, 5, "[-10154] Error at URL Position %lu: the number of items in the $skiptoken list does not match what is expected"),
    INVALID_SKIP_TOKEN_ITEM_TYPE(-10155, 3, 5, "[-10155] Error at URL Position %lu: invalid EDM type of a $skiptoken item; expected \"%s\" but received \"%s\""),
    INVALID_CUSTOM_HEADER(-10156, 3, 13, "[-10156] An invalid custom header was provided. Header name: \"%s\". Header value: \"%s\""),
    SHIM_ALLOCATE_PROPS_ENT_MISSING(-10157, 3, 15, "[-10157] Could not allocate entity properties because an entity was not provided"),
    INVALID_ENTITY_TYPE(-10158, 3, 15, "[-10158] \"%s\" is an invalid entity type"),
    SHIM_ENTITY_ID_MISSING(-10159, 3, 15, "[-10159] Could not allocate entity properties because the provided entity is missing an entity ID"),
    ERROR_FLUSHING_REQUEST_QUEUE(-10160, 2, 3, "[-10160] An error occurred while flushing the queued requests"),
    ERROR_FLUSH_ALREADY_IN_PROGRESS(-10161, 3, 15, "[-10161] Another thread is already flushing the request queue."),
    ADD_TO_ENTITY_SET_DENIED(-10162, 3, 15, "[-10162] Permission to insert an entity in entity set \"%s\".\"%s\" denied."),
    UPDATE_ENTITY_DENIED(-10163, 3, 15, "[-10163] Permission to update \"%s\" denied."),
    ERROR_EXECUTING_REQUEST(-10164, 3, 19, "[-10164] An error occurred while executing a request.  Reason: %s"),
    METHOD_NOT_PROVIDED(-10165, 3, 5, "[-10165] The request method was not provided"),
    INVALID_PATH_FOR_METHOD(-10166, 3, 5, "[-10166] Invalid resource path for a %s request"),
    WRONG_REQUEST_PAYLOAD(-10167, 3, 999, "[-10167] The wrong type of payload was supplied for this request"),
    BATCH_QUERY_OPERATION_EXPECTED(-10168, 3, 999, "[-10168] The batch request failed because a non-read request was found outside of a changeset."),
    EMPTY_BATCH(-10169, 3, 8, "[-10169] The batch request failed because it does not contain any query operations or changesets."),
    EMPTY_CHANGESET(-10170, 3, 8, "[-10170] The batch request failed because it contains an empty change set."),
    INVALID_BATCH_OBJECT(-10171, 3, 999, "[-10171] The batch request failed because it contains unrecognized batch items."),
    INVALID_CHANGESET_OBJECT(-10172, 3, 999, "[-10172] The batch request failed because a changeset contains contains unrecognized items."),
    INVALID_CUSTOM_COOKIE(-10173, 3, 13, "[-10173] An invalid custom cookie was provided. Cookie name: \"%s\". Cookie value: \"%s\""),
    MISSING_HOST(-10174, 3, 13, "[-10174] Error opening the store. No host was provided."),
    INVALID_PORT(-10175, 3, 13, "[-10175] Error opening the store. An invalid port was provided."),
    MISSING_IDENTITY_PASSWORD(-10176, 3, 13, "[-10176] Error opening the store. An identity certificate was provided without also providing an identity password."),
    MISSING_IDENTITY_FILE(-10177, 3, 13, "[-10177] Error opening the store. An identity password was provided without also providing an identity file."),
    IDENTITY_OVER_HTTP(-10178, 3, 13, "[-10178] Error opening the store. An identity certificate cannot be used without enabling Https."),
    CERTIFICATE_OVER_HTTP(-10179, 3, 13, "[-10179] Error opening the store. A trusted certificate cannot be used without enabling Https."),
    READ_IN_CHANGESET(-10181, 3, 999, "[-10181] The batch request failed because it contains a read request within a changeset."),
    INVALID_MAX_PAGE_SIZE(-10182, 3, 15, "[-10182] \"%s\" is an invalid max page size specified in the Prefer header."),
    CANNOT_UPDATE_KEY_PROPERTY(-10183, 3, 15, "[-10183] The modify property request failed because a key property cannot be updated."),
    NULLIFYING_NON_NULL_PROPERTY(-10184, 3, 15, "[-10184] Property \"%s\" cannot be set to null because it is not nullable."),
    INVALID_GUID_VALUE(-10185, 3, 16, "[-10185] The property value \"%s\" is not a valid GUID."),
    STORE_VERSION_TOO_NEW(-10186, 2, 2, "[-10186] This store cannot be opened because it was created by a newer version of the software."),
    ERROR_UPGRADING_STORE_VERSION(-10187, 2, 2, "[-10187] An error occurred while upgrading the store to the current version."),
    NO_ESET_OR_ETYPE_FOR_URL(-10188, 3, 5, "[-10188] The provided URL \"%s\" does not resolve to an entity set or entity type"),
    UNABLE_TO_MOVE_TO_NEW_DB(-10189, 2, 2, "[-10189] An error occurred while moving to a new database.  System specific error code: %ld"),
    REPLACE_DB_FAILED(-10190, 2, 2, "[-10190] Replacing the database failed."),
    SERVICE_ROOT_NOT_ABSOLUTE(-10191, 3, 13, "[-10191] The service root must be an absolute URL."),
    REQUEST_MODE_MISSING(-10192, 3, 4, "[-10192] The request mode is missing."),
    ERROR_DELETE_LINK_NOT_EXIST(-10193, 3, 5, "[-10193] The delete link request failed because no link exists between the provided entities."),
    INVALID_ENTITY_SET_WITH_IS_LOCAL(-10194, 3, 15, "[-10194] Error at URL Position %lu: sap.islocal cannot be used with entity set \"%s.%s\" because it cannot contain local entities"),
    ANDROID_CONTEXT_NOT_PROVIDED(-10195, 3, 15, "[-10195] The Android Context object was not provided"),
    SHIM_UNKNOWN_REQUEST_OBJECT(-10196, 3, 15, "[-10196] The provided request object is of an unknown type."),
    PRODUCER_ERROR(-10197, 2, 4, "[-10197] A request failed against the backend OData producer - code: \"%s\", error: \"%s\", inner error: \"%s\""),
    RESOURCE_NOT_FOUND(-10198, 3, 5, "[-10198] The requested resource could not be found."),
    IOS_SHIM_MISSING_STORE_DELEGATE(-10199, 3, 1, "[-10199] Cannot open the store. The store delegate was not set."),
    JAVA_SHIM_MISSING_STORE_DELEGATE(-10200, 3, 1, "[-10200] Cannot open the store. The store listener was not set."),
    CANNOT_PUT_LOCAL_ENTITY(-10201, 3, 15, "[-10201] Cannot perform a PUT against an entity with an unresolved key property. Use PATCH instead"),
    DROP_DB_IN_USE(-10202, 3, 15, "[-10202] The store could not be dropped. Ensure that the store is closed."),
    NOT_GLOBAL_INIT(-10203, 3, 15, "[-10203] The operation could not be performed because the library has not been globally initialized."),
    SYNC_FAILED_BAD_STATUS_CODE(-10204, 2, 3, "[-10204] Communication with the server failed with status code: %s"),
    SYNC_FAILED_SOCKET_CONNECT(-10205, 2, 3, "[-10205] Failed to establish a socket connection to the server"),
    SYNC_FAILED_INVALID_CHARACTER(-10206, 2, 3, "[-10206] Communication with the server failed. An unexpected character was read in an HTTP header"),
    SYNC_FAILED_AUTH_REQUIRED(-10207, 2, 3, "[-10207] Communication with the server failed due to invalid authentication"),
    SYNC_FAILED_HOST_NOT_FOUND(-10208, 2, 3, "[-10208] Communication with the server because the host could not be found"),
    ANDROID_CERT_STORE_ERROR(-10209, 3, 19, "[-10209] There was an error opening the Android certificate store: %s"),
    SYNC_FAILED_SERVER_ERROR(-10210, 2, 3, "[-10210] The operation failed due to an error on the server."),
    REQUEST_FAILED_SEE_RESPONSE(-10211, 3, 15, "[-10211] The request failed. See the response payload for more details."),
    DEEP_INSERTS_NOT_SUPPORTED(-10212, 3, 15, "[-10212] Deep inserts are not supported with this Offline Store."),
    DEEP_INSERT_TO_ENTITY_SET(-10213, 3, 15, "[-10213] Deep inserts to entity sets are not supported"),
    TOO_MANY_SYSTEM_REFRESHES(-10214, 3, 15, "[-10214] The Offline Store has been requested to perform a refresh too many times consecutively from the server.  The Offline Store is being closed automatically."),
    MEDIA_STREAMS_NOT_SUPPORTED(-10215, 3, 13, "[-10215] Media streams are not supported by the current offline store or the server it is communicating with."),
    GENERATED_URL_FOR_STREAM_REQUEST(-10216, 3, 13, "[-10216] \"%s\" is a generated URL and cannot be used to register a media stream request."),
    INVALID_STREAM_URL(-10217, 3, 13, "[-10217] \"%s\" is not a media entity.  A URL that references a media entity is required when registering a media stream request."),
    NON_MEDIA_ENTITY_IN_STREAM_REQUEST(-10218, 3, 13, "[-10218] \"%s.%s\" is not a media entity and therefore cannot be used to register a media stream request."),
    NO_STREAM_REQUEST(-10219, 3, 13, "[-10219] \"%s\" is not a media stream request and therefore cannot be unregistered."),
    EXPAND_IN_STREAM_URL(-10220, 3, 13, "[-10220] \"%s\" contains an $expand system query option and therefore cannot be registered."),
    STREAMING_TO_INVALID_URL(-10221, 3, 15, "[-10221] The provided URL is not valid for modifying a media stream."),
    NO_INPUTSTREAM_IN_MEDIA_RESOURCE(-10222, 3, 17, "[-10222] No input stream was provided in the media stream payload."),
    NO_CONTENT_TYPE_IN_MEDIA_RESOURCE(-10223, 3, 17, "[-10223] No content type was provided in the media stream payload."),
    ERROR_READING_FROM_INPUTSTREAM(-10224, 3, 17, "[-10224] Failed to read data from the input stream provided in the media stream payload."),
    MEDIA_STREAM_ALREADY_AVAILABLE(-10225, 3, 17, "[-10225] The media stream for \"%s\" is already available in the offline store."),
    MEDIA_ENTITY_DOES_NOT_EXIST(-10226, 3, 13, "[-10226] \"%s\" does not exist and therefore cannot be used to register a media stream request."),
    REQUEST_EXISTS(-10227, 3, 13, "[-10227] A request with name \"%s\" already exists."),
    UNSUPPORTED_STREAM_IN_BATCH(-10228, 3, 17, "[-10228] Reading or modifying a media stream in a batch request is not supported."),
    INVALID_STREAM_REQUEST_NAME(-10229, 3, 13, "[-10229] An empty or null media stream request name was provided."),
    STREAM_REQUEST_NAME_TOO_LONG(-10230, 3, 13, "[-10230] A name provided for a media stream request exceeded the maximum allowable length of %lu: \"%s\""),
    ANDROID_STREAM_LISTENER_MISSING(-10231, 3, 13, "[-10231] No listener was provided for a call to read a media stream."),
    WRONG_METHOD_FOR_STREAM_READ(-10232, 3, 13, "[-10232] Media streams cannot be read using this method."),
    WRONG_METHOD_FOR_NON_STREAM_READ(-10233, 3, 13, "[-10233] Only media streams can be read using this method."),
    REQUEST_FOR_MEDIA_STREAM_EXISTS(-10234, 3, 13, "[-10234] A registered media stream request already exists for \"%s\"."),
    OPENING_STORE(5000, 0, 0, "[5000] Opening the store."),
    STORE_STATE_OPENING(5001, 0, 0, "[5001] The store state has changed to \'Opening\'"),
    STORE_STATE_INITIALIZING(5002, 0, 0, "[5002] The store state has changed to \'Initializing\'"),
    STORE_STATE_POPULATING(5003, 0, 0, "[5003] The store state has changed to \'Populating\'"),
    STORE_STATE_DOWNLOADING(5004, 0, 0, "[5004] The store state has changed to \'Downloading\'"),
    STORE_STATE_OPEN(5005, 0, 0, "[5005] The store state has changed to \'Open\'"),
    STORE_STATE_CLOSED(5006, 0, 0, "[5006] The store state has changed to \'Closed\'"),
    STORE_STATE_ERROR(5007, 0, 0, "[5007] The store state has changed to \'Error\'"),
    CALLING_REQUEST_FAILED_DELEGATE(5009, 0, 0, "[5009] Calling the request failure callback for request with custom tag \"%s\""),
    LOG_STORE_OPTION(5010, 0, 0, "[5010] Store option %s set to \"%s\"."),
    LOG_DEFINING_REQUEST(5011, 0, 0, "[5011] Defining request defined with name \"%s\" and URL \"%s\" and retrieve streams \"%s\"."),
    LOG_CUSTOM_HEADER(5012, 0, 0, "[5012] Custom header defined with name \"%s\" and value \"%s\"."),
    LOG_CUSTOM_COOKIE(5013, 0, 0, "[5013] Custom cookie defined with name \"%s\" and value \"%s\"."),
    LOG_STORE_OPTION_NOT_SET(5014, 0, 0, "[5014] Store option %s was not set."),
    FLUSHING_QUEUED_REQUESTS(5015, 0, 0, "[5015] Flushed the request queue."),
    FINISHED_FLUSH(5016, 0, 0, "[5016] Finished flushing the request queue."),
    REFRESHING_STORE(5017, 0, 0, "[5017] Refreshing the store."),
    FINISHED_REFRESHING_STORE(5018, 0, 0, "[5018] Finished refreshing the store."),
    ALLOCATING_PROPERTIES(5019, 0, 0, "[5019] Allocating properties for entity of type \"%s\"."),
    ALLOCATING_NAV_PROPERTIES(5020, 0, 0, "[5020] Allocating navigation properties for entity of type \"%s\"."),
    CLOSING_STORE(5021, 0, 0, "[5021] Closing the store."),
    HAS_PENDING_REFRESH(5022, 0, 0, "[5022] The store was closed while performing a refresh."),
    HAS_PENDING_FLUSH(5023, 0, 0, "[5023] The store was closed while flushing the request queue."),
    SHIM_CONV_MANAGER_NOT_PROVIDED(5024, 0, 0, "[5024] No Http Conversation Manager was provided."),
    ENCRYPT_KEY_NOT_PROVIDED(5025, 0, 0, "[5025] No store encryption key was provided.  The store will be created unencrypted."),
    MERGE_PARAMS(5026, 0, 0, "[5026] Merged stream parameters: \"%s\"."),
    REGISTERING_STREAM_REQUEST(5027, 0, 0, "[5027] Registering a stream request with name \"%s\" and resource path \"%s\"."),
    UNREGISTERING_STREAM_REQUEST(5028, 0, 0, "[5028] Unregistering a stream request with name \"%s\".");

    private final int code;
    private final int hapidom;
    private final int hapicode;
    private final String message;
    private static final HashMap<Integer, MessageCode> codeMap;

    private MessageCode(int var3, int var4, int var5, String var6) {
        this.code = var3;
        this.hapidom = var4;
        this.hapicode = var5;
        this.message = var6;
    }

    public static MessageCode getByCode(int var0) {
        return (MessageCode)codeMap.get(Integer.valueOf(var0));
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }

    public int getHAPIDomain() {
        return this.hapidom;
    }

    public int getHAPICode() {
        return this.hapicode;
    }

    static {
        codeMap = new HashMap();
        codeMap.put(Integer.valueOf(0), OKAY);
        codeMap.put(Integer.valueOf(-10000), DEBUG_EXCEPTION);
        codeMap.put(Integer.valueOf(-10001), ERROR_EXECUTING_SQL_STMT);
        codeMap.put(Integer.valueOf(-10005), ERROR_CONNECTING_TO_DB);
        codeMap.put(Integer.valueOf(-10006), DB_NOT_FOUND);
        codeMap.put(Integer.valueOf(-10010), ERROR_LOADING_EDM_METADATA);
        codeMap.put(Integer.valueOf(-10016), ERROR_LOADING_MAPPING);
        codeMap.put(Integer.valueOf(-10021), FATAL_PARSE_ERROR);
        codeMap.put(Integer.valueOf(-10022), URL_SYNTAX_ERROR);
        codeMap.put(Integer.valueOf(-10023), UNEXPECTED_EOI);
        codeMap.put(Integer.valueOf(-10024), ILLEGAL_TYPE_VALUE);
        codeMap.put(Integer.valueOf(-10025), ILLEGAL_USE_OF_KEYWORD);
        codeMap.put(Integer.valueOf(-10026), EMPTY_RESOURCE_PATH);
        codeMap.put(Integer.valueOf(-10027), INVALID_ENTITY_CONTAINER);
        codeMap.put(Integer.valueOf(-10028), INVALID_ENTITY_SET);
        codeMap.put(Integer.valueOf(-10029), INVALID_KEY_PREDICATE);
        codeMap.put(Integer.valueOf(-10030), INVALID_UNNAMED_KEY_VALUE);
        codeMap.put(Integer.valueOf(-10031), WRONG_NUMBER_OF_KEY_VALUES);
        codeMap.put(Integer.valueOf(-10032), INVALID_KEY_PROPERTY);
        codeMap.put(Integer.valueOf(-10033), DUPLICATE_KEY_PROPERTY);
        codeMap.put(Integer.valueOf(-10034), INVALID_KEY_PROPERTY_TYPE);
        codeMap.put(Integer.valueOf(-10035), INVALID_NULL_IN_KEY_PREDICATE);
        codeMap.put(Integer.valueOf(-10036), INVALID_PROPERTY);
        codeMap.put(Integer.valueOf(-10037), INVALID_EMPTY_PAREN);
        codeMap.put(Integer.valueOf(-10038), INVALID_PATH_SEGMENT_AFTER_PROP);
        codeMap.put(Integer.valueOf(-10039), INVALID_PATH_SEGMENT_AFTER_ESET);
        codeMap.put(Integer.valueOf(-10046), ERROR_SETTING_NULL_PARAMETER);
        codeMap.put(Integer.valueOf(-10047), ERROR_SETTING_PARAMETER);
        codeMap.put(Integer.valueOf(-10048), DUPLICATE_QUERY_OPTION);
        codeMap.put(Integer.valueOf(-10049), NEGATIVE_TOP);
        codeMap.put(Integer.valueOf(-10050), NEGATIVE_SKIP);
        codeMap.put(Integer.valueOf(-10051), ORDER_BY_MUST_END_WITH_PROPERTY);
        codeMap.put(Integer.valueOf(-10052), TOP_TOO_LARGE);
        codeMap.put(Integer.valueOf(-10053), SKIP_TOO_LARGE);
        codeMap.put(Integer.valueOf(-10054), ERROR_LOADING_DB_METADATA);
        codeMap.put(Integer.valueOf(-10055), STORE_ALREADY_OPEN);
        codeMap.put(Integer.valueOf(-10056), STORE_PREVIOUSLY_CLOSED);
        codeMap.put(Integer.valueOf(-10057), ERROR_READING_METADATA_FROM_DB);
        codeMap.put(Integer.valueOf(-10058), STORE_NOT_OPEN);
        codeMap.put(Integer.valueOf(-10059), ERROR_EXECUTING_GET_REQUEST);
        codeMap.put(Integer.valueOf(-10060), ERROR_SYNCHRONIZING);
        codeMap.put(Integer.valueOf(-10061), ERROR_EXPANDING_DEPLOY_FILE);
        codeMap.put(Integer.valueOf(-10062), ERROR_INITIALIZING_STORE);
        codeMap.put(Integer.valueOf(-10063), UNSUPPORTED_KEY_WORD);
        codeMap.put(Integer.valueOf(-10064), UNABLE_TO_DELETE_DB);
        codeMap.put(Integer.valueOf(-10065), DOWNLOAD_DB_FAILED);
        codeMap.put(Integer.valueOf(-10066), ERROR_VALIDATING_DB);
        codeMap.put(Integer.valueOf(-10067), COULD_NOT_CREATE_DB);
        codeMap.put(Integer.valueOf(-10069), INVALID_PROPERTY_VALUE_TYPE);
        codeMap.put(Integer.valueOf(-10070), INVALID_USE_OF_EXPAND);
        codeMap.put(Integer.valueOf(-10071), INVALID_NAVIGATION_PROPERTY);
        codeMap.put(Integer.valueOf(-10072), INVALID_USE_OF_SELECT);
        codeMap.put(Integer.valueOf(-10073), SELECT_OF_NON_EXPANDED_ITEM);
        codeMap.put(Integer.valueOf(-10074), FOLLOWED_PROPERTY_IN_SELECT);
        codeMap.put(Integer.valueOf(-10075), INVALID_USE_OF_TOP);
        codeMap.put(Integer.valueOf(-10076), INVALID_USE_OF_SKIP);
        codeMap.put(Integer.valueOf(-10077), ORDER_BY_OF_NON_EXPANDED_ITEM);
        codeMap.put(Integer.valueOf(-10078), INVALID_USE_OF_ORDER_BY);
        codeMap.put(Integer.valueOf(-10080), ERROR_PREPARING_SQL_STMT);
        codeMap.put(Integer.valueOf(-10081), ERROR_STMT_NOT_PREPARED);
        codeMap.put(Integer.valueOf(-10082), INVALID_USE_OF_FILTER);
        codeMap.put(Integer.valueOf(-10083), FILTER_MUST_BE_BOOLEAN);
        codeMap.put(Integer.valueOf(-10084), INVALID_ARITHMETIC_TYPE);
        codeMap.put(Integer.valueOf(-10085), TYPES_NOT_PROMOTABLE);
        codeMap.put(Integer.valueOf(-10086), GENERIC_FILTER_ERROR);
        codeMap.put(Integer.valueOf(-10087), TYPES_NOT_COMPARABLE);
        codeMap.put(Integer.valueOf(-10088), BOOLEAN_RELATION_OP_ERROR);
        codeMap.put(Integer.valueOf(-10089), INVALID_BOOLEAN_TYPE);
        codeMap.put(Integer.valueOf(-10090), INVALID_EDM_TYPE_NAME);
        codeMap.put(Integer.valueOf(-10091), UNSUPPORTED_METHOD_CALL);
        codeMap.put(Integer.valueOf(-10092), UNKNOWN_METHOD_CALL);
        codeMap.put(Integer.valueOf(-10093), INVALID_PARAMETER_TYPE);
        codeMap.put(Integer.valueOf(-10094), INVALID_PARAMETER_COUNT);
        codeMap.put(Integer.valueOf(-10095), CAST_TO_BOOLEAN);
        codeMap.put(Integer.valueOf(-10096), INVALID_MEMBER_EXPRESSION_END);
        codeMap.put(Integer.valueOf(-10097), INVALID_NAV_PROP_MULTIPLICITY);
        codeMap.put(Integer.valueOf(-10098), INVALID_CAST_EDM_TYPE);
        codeMap.put(Integer.valueOf(-10099), INVALID_PATH_SEGMENT_AFTER_CTYPE);
        codeMap.put(Integer.valueOf(-10100), INVALID_PATH_SEGMENT_AFTER_ETYPE);
        codeMap.put(Integer.valueOf(-10101), NO_METADATA_DOC);
        codeMap.put(Integer.valueOf(-10102), INVALID_BINARY_OP_OPERAND_TYPE);
        codeMap.put(Integer.valueOf(-10103), ERROR_EXECUTING_MOD_REQUEST);
        codeMap.put(Integer.valueOf(-10104), ERROR_COMMITTING_DB_TRANSACTION);
        codeMap.put(Integer.valueOf(-10105), ERROR_GENERATING_ENTITY_ID);
        codeMap.put(Integer.valueOf(-10106), INVALID_LINK_ADDRESS);
        codeMap.put(Integer.valueOf(-10107), ERROR_UPDATE_ENTITY_NOT_EXIST);
        codeMap.put(Integer.valueOf(-10108), INVALID_ENTITY_URL);
        codeMap.put(Integer.valueOf(-10110), NO_SERVICE_DOC);
        codeMap.put(Integer.valueOf(-10111), FATAL_INTERNAL_ERROR);
        codeMap.put(Integer.valueOf(-10112), INVALID_PATH_SEGMENT_AFTER_LINKS);
        codeMap.put(Integer.valueOf(-10113), INVALID_PATH_FOR_CREATE_ENTITY);
        codeMap.put(Integer.valueOf(-10114), INVALID_PATH_FOR_MODIFY_ENTITY);
        codeMap.put(Integer.valueOf(-10115), INVALID_PATH_FOR_MODIFY_LINK);
        codeMap.put(Integer.valueOf(-10116), INVALID_PATH_FOR_MODIFY_LINKS);
        codeMap.put(Integer.valueOf(-10117), INVALID_PATH_FOR_MODIFY_PROPERTY);
        codeMap.put(Integer.valueOf(-10118), INVALID_PATH_FOR_MODIFY_VALUE);
        codeMap.put(Integer.valueOf(-10119), ERROR_UPDATE_ENTITY_WRONG_TYPE);
        codeMap.put(Integer.valueOf(-10120), ERROR_ADDING_LINK_WRONG_TYPE);
        codeMap.put(Integer.valueOf(-10121), ERROR_ADDING_LINK_ALREADY_EXISTS);
        codeMap.put(Integer.valueOf(-10124), INVALID_USE_OF_INLINECOUNT);
        codeMap.put(Integer.valueOf(-10125), CANNOT_DELETE_PRIMARY_ENTITY);
        codeMap.put(Integer.valueOf(-10127), ERROR_GENERATING_ETAG);
        codeMap.put(Integer.valueOf(-10128), ERROR_ETAGS_DIFFER);
        codeMap.put(Integer.valueOf(-10129), LINK_SOURCE_NOT_EXIST);
        codeMap.put(Integer.valueOf(-10130), LINK_LINKED_NOT_EXIST);
        codeMap.put(Integer.valueOf(-10131), MODIFY_LINK_VIOLATES_CARDINALITY);
        codeMap.put(Integer.valueOf(-10132), INVALID_REFRESH_SUBSET);
        codeMap.put(Integer.valueOf(-10133), DEFINING_REQUEST_NOT_FOUND);
        codeMap.put(Integer.valueOf(-10134), INVALID_DEFINING_REQUEST);
        codeMap.put(Integer.valueOf(-10135), REQUEST_NAME_TOO_LONG);
        codeMap.put(Integer.valueOf(-10136), INVALID_ENTITY_EXISTS_PARAMETER);
        codeMap.put(Integer.valueOf(-10137), SHIM_STORE_OPTIONS_NOT_PROVIDED);
        codeMap.put(Integer.valueOf(-10138), MISSING_SERVICE_ROOT);
        codeMap.put(Integer.valueOf(-10139), INVALID_PAGE_SIZE);
        codeMap.put(Integer.valueOf(-10140), MISSING_ML_STREAM_PARMS);
        codeMap.put(Integer.valueOf(-10141), MISSING_USERNAME);
        codeMap.put(Integer.valueOf(-10142), MISSING_PASSWORD);
        codeMap.put(Integer.valueOf(-10143), MISSING_STORE_NAME);
        codeMap.put(Integer.valueOf(-10144), MISSING_STORE_PATH);
        codeMap.put(Integer.valueOf(-10145), SHIM_MISSING_REQUEST);
        codeMap.put(Integer.valueOf(-10146), REQUEST_TYPE_NOT_SUPPORTED);
        codeMap.put(Integer.valueOf(-10147), MISSING_PAYLOAD);
        codeMap.put(Integer.valueOf(-10148), INVALID_PAYLOAD_TYPE);
        codeMap.put(Integer.valueOf(-10149), DROP_DB_NOT_FOUND);
        codeMap.put(Integer.valueOf(-10150), COULD_NOT_DROP_DB);
        codeMap.put(Integer.valueOf(-10151), ERROR_COMPUTING_BASIC_AUTH);
        codeMap.put(Integer.valueOf(-10152), INVALID_USE_OF_SKIP_TOKEN);
        codeMap.put(Integer.valueOf(-10153), INVALID_SKIP_WITH_SKIP_TOKEN);
        codeMap.put(Integer.valueOf(-10154), INVALID_SKIP_TOKEN_ITEM_COUNT);
        codeMap.put(Integer.valueOf(-10155), INVALID_SKIP_TOKEN_ITEM_TYPE);
        codeMap.put(Integer.valueOf(-10156), INVALID_CUSTOM_HEADER);
        codeMap.put(Integer.valueOf(-10157), SHIM_ALLOCATE_PROPS_ENT_MISSING);
        codeMap.put(Integer.valueOf(-10158), INVALID_ENTITY_TYPE);
        codeMap.put(Integer.valueOf(-10159), SHIM_ENTITY_ID_MISSING);
        codeMap.put(Integer.valueOf(-10160), ERROR_FLUSHING_REQUEST_QUEUE);
        codeMap.put(Integer.valueOf(-10161), ERROR_FLUSH_ALREADY_IN_PROGRESS);
        codeMap.put(Integer.valueOf(-10162), ADD_TO_ENTITY_SET_DENIED);
        codeMap.put(Integer.valueOf(-10163), UPDATE_ENTITY_DENIED);
        codeMap.put(Integer.valueOf(-10164), ERROR_EXECUTING_REQUEST);
        codeMap.put(Integer.valueOf(-10165), METHOD_NOT_PROVIDED);
        codeMap.put(Integer.valueOf(-10166), INVALID_PATH_FOR_METHOD);
        codeMap.put(Integer.valueOf(-10167), WRONG_REQUEST_PAYLOAD);
        codeMap.put(Integer.valueOf(-10168), BATCH_QUERY_OPERATION_EXPECTED);
        codeMap.put(Integer.valueOf(-10169), EMPTY_BATCH);
        codeMap.put(Integer.valueOf(-10170), EMPTY_CHANGESET);
        codeMap.put(Integer.valueOf(-10171), INVALID_BATCH_OBJECT);
        codeMap.put(Integer.valueOf(-10172), INVALID_CHANGESET_OBJECT);
        codeMap.put(Integer.valueOf(-10173), INVALID_CUSTOM_COOKIE);
        codeMap.put(Integer.valueOf(-10174), MISSING_HOST);
        codeMap.put(Integer.valueOf(-10175), INVALID_PORT);
        codeMap.put(Integer.valueOf(-10176), MISSING_IDENTITY_PASSWORD);
        codeMap.put(Integer.valueOf(-10177), MISSING_IDENTITY_FILE);
        codeMap.put(Integer.valueOf(-10178), IDENTITY_OVER_HTTP);
        codeMap.put(Integer.valueOf(-10179), CERTIFICATE_OVER_HTTP);
        codeMap.put(Integer.valueOf(-10181), READ_IN_CHANGESET);
        codeMap.put(Integer.valueOf(-10182), INVALID_MAX_PAGE_SIZE);
        codeMap.put(Integer.valueOf(-10183), CANNOT_UPDATE_KEY_PROPERTY);
        codeMap.put(Integer.valueOf(-10184), NULLIFYING_NON_NULL_PROPERTY);
        codeMap.put(Integer.valueOf(-10185), INVALID_GUID_VALUE);
        codeMap.put(Integer.valueOf(-10186), STORE_VERSION_TOO_NEW);
        codeMap.put(Integer.valueOf(-10187), ERROR_UPGRADING_STORE_VERSION);
        codeMap.put(Integer.valueOf(-10188), NO_ESET_OR_ETYPE_FOR_URL);
        codeMap.put(Integer.valueOf(-10189), UNABLE_TO_MOVE_TO_NEW_DB);
        codeMap.put(Integer.valueOf(-10190), REPLACE_DB_FAILED);
        codeMap.put(Integer.valueOf(-10191), SERVICE_ROOT_NOT_ABSOLUTE);
        codeMap.put(Integer.valueOf(-10192), REQUEST_MODE_MISSING);
        codeMap.put(Integer.valueOf(-10193), ERROR_DELETE_LINK_NOT_EXIST);
        codeMap.put(Integer.valueOf(-10194), INVALID_ENTITY_SET_WITH_IS_LOCAL);
        codeMap.put(Integer.valueOf(-10195), ANDROID_CONTEXT_NOT_PROVIDED);
        codeMap.put(Integer.valueOf(-10196), SHIM_UNKNOWN_REQUEST_OBJECT);
        codeMap.put(Integer.valueOf(-10197), PRODUCER_ERROR);
        codeMap.put(Integer.valueOf(-10198), RESOURCE_NOT_FOUND);
        codeMap.put(Integer.valueOf(-10199), IOS_SHIM_MISSING_STORE_DELEGATE);
        codeMap.put(Integer.valueOf(-10200), JAVA_SHIM_MISSING_STORE_DELEGATE);
        codeMap.put(Integer.valueOf(-10201), CANNOT_PUT_LOCAL_ENTITY);
        codeMap.put(Integer.valueOf(-10202), DROP_DB_IN_USE);
        codeMap.put(Integer.valueOf(-10203), NOT_GLOBAL_INIT);
        codeMap.put(Integer.valueOf(-10204), SYNC_FAILED_BAD_STATUS_CODE);
        codeMap.put(Integer.valueOf(-10205), SYNC_FAILED_SOCKET_CONNECT);
        codeMap.put(Integer.valueOf(-10206), SYNC_FAILED_INVALID_CHARACTER);
        codeMap.put(Integer.valueOf(-10207), SYNC_FAILED_AUTH_REQUIRED);
        codeMap.put(Integer.valueOf(-10208), SYNC_FAILED_HOST_NOT_FOUND);
        codeMap.put(Integer.valueOf(-10209), ANDROID_CERT_STORE_ERROR);
        codeMap.put(Integer.valueOf(-10210), SYNC_FAILED_SERVER_ERROR);
        codeMap.put(Integer.valueOf(-10211), REQUEST_FAILED_SEE_RESPONSE);
        codeMap.put(Integer.valueOf(-10212), DEEP_INSERTS_NOT_SUPPORTED);
        codeMap.put(Integer.valueOf(-10213), DEEP_INSERT_TO_ENTITY_SET);
        codeMap.put(Integer.valueOf(-10214), TOO_MANY_SYSTEM_REFRESHES);
        codeMap.put(Integer.valueOf(-10215), MEDIA_STREAMS_NOT_SUPPORTED);
        codeMap.put(Integer.valueOf(-10216), GENERATED_URL_FOR_STREAM_REQUEST);
        codeMap.put(Integer.valueOf(-10217), INVALID_STREAM_URL);
        codeMap.put(Integer.valueOf(-10218), NON_MEDIA_ENTITY_IN_STREAM_REQUEST);
        codeMap.put(Integer.valueOf(-10219), NO_STREAM_REQUEST);
        codeMap.put(Integer.valueOf(-10220), EXPAND_IN_STREAM_URL);
        codeMap.put(Integer.valueOf(-10221), STREAMING_TO_INVALID_URL);
        codeMap.put(Integer.valueOf(-10222), NO_INPUTSTREAM_IN_MEDIA_RESOURCE);
        codeMap.put(Integer.valueOf(-10223), NO_CONTENT_TYPE_IN_MEDIA_RESOURCE);
        codeMap.put(Integer.valueOf(-10224), ERROR_READING_FROM_INPUTSTREAM);
        codeMap.put(Integer.valueOf(-10225), MEDIA_STREAM_ALREADY_AVAILABLE);
        codeMap.put(Integer.valueOf(-10226), MEDIA_ENTITY_DOES_NOT_EXIST);
        codeMap.put(Integer.valueOf(-10227), REQUEST_EXISTS);
        codeMap.put(Integer.valueOf(-10228), UNSUPPORTED_STREAM_IN_BATCH);
        codeMap.put(Integer.valueOf(-10229), INVALID_STREAM_REQUEST_NAME);
        codeMap.put(Integer.valueOf(-10230), STREAM_REQUEST_NAME_TOO_LONG);
        codeMap.put(Integer.valueOf(-10231), ANDROID_STREAM_LISTENER_MISSING);
        codeMap.put(Integer.valueOf(-10232), WRONG_METHOD_FOR_STREAM_READ);
        codeMap.put(Integer.valueOf(-10233), WRONG_METHOD_FOR_NON_STREAM_READ);
        codeMap.put(Integer.valueOf(-10234), REQUEST_FOR_MEDIA_STREAM_EXISTS);
        codeMap.put(Integer.valueOf(5000), OPENING_STORE);
        codeMap.put(Integer.valueOf(5001), STORE_STATE_OPENING);
        codeMap.put(Integer.valueOf(5002), STORE_STATE_INITIALIZING);
        codeMap.put(Integer.valueOf(5003), STORE_STATE_POPULATING);
        codeMap.put(Integer.valueOf(5004), STORE_STATE_DOWNLOADING);
        codeMap.put(Integer.valueOf(5005), STORE_STATE_OPEN);
        codeMap.put(Integer.valueOf(5006), STORE_STATE_CLOSED);
        codeMap.put(Integer.valueOf(5007), STORE_STATE_ERROR);
        codeMap.put(Integer.valueOf(5009), CALLING_REQUEST_FAILED_DELEGATE);
        codeMap.put(Integer.valueOf(5010), LOG_STORE_OPTION);
        codeMap.put(Integer.valueOf(5011), LOG_DEFINING_REQUEST);
        codeMap.put(Integer.valueOf(5012), LOG_CUSTOM_HEADER);
        codeMap.put(Integer.valueOf(5013), LOG_CUSTOM_COOKIE);
        codeMap.put(Integer.valueOf(5014), LOG_STORE_OPTION_NOT_SET);
        codeMap.put(Integer.valueOf(5015), FLUSHING_QUEUED_REQUESTS);
        codeMap.put(Integer.valueOf(5016), FINISHED_FLUSH);
        codeMap.put(Integer.valueOf(5017), REFRESHING_STORE);
        codeMap.put(Integer.valueOf(5018), FINISHED_REFRESHING_STORE);
        codeMap.put(Integer.valueOf(5019), ALLOCATING_PROPERTIES);
        codeMap.put(Integer.valueOf(5020), ALLOCATING_NAV_PROPERTIES);
        codeMap.put(Integer.valueOf(5021), CLOSING_STORE);
        codeMap.put(Integer.valueOf(5022), HAS_PENDING_REFRESH);
        codeMap.put(Integer.valueOf(5023), HAS_PENDING_FLUSH);
        codeMap.put(Integer.valueOf(5024), SHIM_CONV_MANAGER_NOT_PROVIDED);
        codeMap.put(Integer.valueOf(5025), ENCRYPT_KEY_NOT_PROVIDED);
        codeMap.put(Integer.valueOf(5026), MERGE_PARAMS);
        codeMap.put(Integer.valueOf(5027), REGISTERING_STREAM_REQUEST);
        codeMap.put(Integer.valueOf(5028), UNREGISTERING_STREAM_REQUEST);
    }
}
